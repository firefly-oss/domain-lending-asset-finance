# domain-lending-asset-finance

> Domain-layer orchestration microservice for asset finance workflows, covering the full lifecycle of renting and leasing agreements.

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Domain Logic](#domain-logic)
- [Dependencies](#dependencies)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [Testing](#testing)

## Overview

`domain-lending-asset-finance` is a reactive Spring Boot microservice that acts as the domain-layer orchestrator for asset finance operations within the Firefly lending platform. It sits between experience-layer consumers (mobile, web, BFF services) and the core service `core-lending-asset-finance`, coordinating all business operations through a CQRS command/query bus.

The service manages the complete lifecycle of asset finance agreements — including agreement creation and updates, physical asset registration, delivery and pickup logistics, return and condition assessments, service event logging, usage reporting, and lease-end option management. All interactions are fully reactive, built on Spring WebFlux and Project Reactor.

Rather than owning persistent state, this service delegates every write and read operation to `core-lending-asset-finance` via its generated WebClient-based SDK (`core-lending-asset-finance-sdk`). This clean separation keeps domain orchestration logic decoupled from persistence concerns and ensures a single source of truth for asset finance data.

## Architecture

The service follows the **CQRS (Command Query Responsibility Segregation)** pattern provided by `fireflyframework-starter-domain`. Incoming HTTP requests are translated into typed `Command` or `Query` objects, dispatched through a `CommandBus` or `QueryBus`, and handled by dedicated handler classes that call the downstream core service SDK.

```
Experience Layer (BFF / Mobile / Web)
            |
            v
  [ domain-lending-asset-finance ]  :8043
            |
     AssetFinanceController
            |
     AssetFinanceServiceImpl
            |
    CommandBus / QueryBus  (fireflyframework CQRS)
            |
    Command/Query Handlers
            |
     core-lending-asset-finance-sdk  (WebClient)
            |
            v
  [ core-lending-asset-finance ]  :8090
```

Event-driven integration is enabled via `fireflyframework-eda` with Kafka as the default publisher. Step events are also enabled to allow the framework to emit execution telemetry. CQRS commands carry a 30-second timeout; queries carry a 15-second timeout with a 15-minute query cache TTL.

## Module Structure

| Module | Purpose |
|--------|---------|
| `domain-lending-asset-finance-interfaces` | Domain-level DTOs exposed to experience-layer consumers (e.g., `AssetFinanceAgreementSummaryDTO`). Does not own persistence; depends on `core`. |
| `domain-lending-asset-finance-core` | CQRS commands, queries, command/query handlers, and the `AssetFinanceService` interface with its implementation. All downstream SDK calls originate here. |
| `domain-lending-asset-finance-infra` | Infrastructure concerns: `AssetFinanceClientFactory` (instantiates WebClient-based SDK API beans) and `AssetFinanceProperties` (binds `api-configuration.core-lending.asset-finance` config). |
| `domain-lending-asset-finance-web` | Spring Boot application entry point, `AssetFinanceController` (REST layer), OpenAPI/Swagger configuration, and web-layer unit tests. |
| `domain-lending-asset-finance-sdk` | Auto-generated client SDK for this service, produced by the OpenAPI Generator Maven plugin from the service's own OpenAPI spec. Consumed by experience-layer services. |

## API Endpoints

All endpoints are prefixed with `/api/v1/asset-finance` and produce/consume `application/json`. All path variables that identify resources are UUIDs.

### Agreements

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `POST` | `/agreements` | Create a new asset finance agreement | `201 Created` |
| `GET` | `/agreements/{agreementId}` | Retrieve an agreement by ID | `200 OK` / `404 Not Found` |
| `PUT` | `/agreements/{agreementId}` | Update an existing agreement | `200 OK` / `404 Not Found` |

### Assets

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `GET` | `/agreements/{agreementId}/assets` | List all assets registered under an agreement (paginated) | `200 OK` |
| `POST` | `/agreements/{agreementId}/assets` | Register a physical asset under an agreement | `201 Created` |
| `GET` | `/agreements/{agreementId}/assets/{assetId}` | Retrieve a single asset by ID | `200 OK` / `404 Not Found` |

### Deliveries

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `GET` | `/agreements/{agreementId}/assets/{assetId}/deliveries` | List all delivery records for an asset (paginated) | `200 OK` |
| `POST` | `/agreements/{agreementId}/assets/{assetId}/deliveries` | Register a delivery record for an asset | `201 Created` |

### Pickups

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `GET` | `/agreements/{agreementId}/assets/{assetId}/pickups` | List all pickup records for an asset (paginated) | `200 OK` |
| `POST` | `/agreements/{agreementId}/assets/{assetId}/pickups` | Register a pickup record for an asset | `201 Created` |

### Returns

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `GET` | `/agreements/{agreementId}/assets/{assetId}/returns` | List all return records for an asset (paginated) | `200 OK` |
| `POST` | `/agreements/{agreementId}/assets/{assetId}/returns` | Register a return and condition assessment for an asset | `201 Created` |

### Service Events

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `GET` | `/agreements/{agreementId}/assets/{assetId}/service-events` | List all service events logged for an asset (paginated) | `200 OK` |
| `POST` | `/agreements/{agreementId}/assets/{assetId}/service-events` | Log a service event (maintenance, damage, inspection) for an asset | `201 Created` |

### Usage

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `GET` | `/agreements/{agreementId}/assets/{assetId}/usage` | List all usage snapshots for an asset (paginated) | `200 OK` |
| `POST` | `/agreements/{agreementId}/assets/{assetId}/usage` | Record a usage snapshot (mileage, operating hours) for an asset | `201 Created` |

### End Options

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `GET` | `/agreements/{agreementId}/end-options` | List all lease-end purchase options for an agreement (paginated) | `200 OK` |
| `POST` | `/agreements/{agreementId}/end-options` | Create a lease-end purchase option for an agreement | `201 Created` |
| `PUT` | `/agreements/{agreementId}/end-options/{optionId}` | Update a lease-end option (e.g., mark it as exercised) | `200 OK` / `404 Not Found` |

OpenAPI documentation (Swagger UI) is available at `/swagger-ui.html` and the raw spec at `/v3/api-docs` (disabled in production profile).

## Domain Logic

The domain service layer (`AssetFinanceServiceImpl`) is a pure dispatcher: it constructs typed command or query objects and sends them to the appropriate bus. All business logic resides in the individual handlers.

### Command Handlers

Each command handler extends `CommandHandler<C, R>` and is annotated with `@CommandHandlerComponent` so the CQRS framework auto-registers it. Every write command passes a freshly generated `UUID` as an idempotency key to the downstream SDK.

| Handler | Command | Downstream SDK Call |
|---------|---------|---------------------|
| `CreateAgreementHandler` | `CreateAgreementCommand(dto)` | `AssetFinanceAgreementApi.create(dto, idempotencyKey)` |
| `UpdateAgreementHandler` | `UpdateAgreementCommand(agreementId, dto)` | `AssetFinanceAgreementApi.update(agreementId, dto, idempotencyKey)` |
| `RegisterAssetHandler` | `RegisterAssetCommand(agreementId, dto)` | `AssetFinanceAssetApi.create2(agreementId, dto, idempotencyKey)` |
| `RegisterDeliveryHandler` | `RegisterDeliveryCommand(agreementId, assetId, dto)` | `DeliveryRecordApi.create7(agreementId, assetId, dto, idempotencyKey)` |
| `RegisterPickupHandler` | `RegisterPickupCommand(agreementId, assetId, dto)` | `PickupRecordApi.create6(agreementId, assetId, dto, idempotencyKey)` |
| `RegisterReturnHandler` | `RegisterReturnCommand(agreementId, assetId, dto)` | `ReturnRecordApi.create5(agreementId, assetId, dto, idempotencyKey)` |
| `RegisterServiceEventHandler` | `RegisterServiceEventCommand(agreementId, assetId, dto)` | `ServiceEventApi.create4(agreementId, assetId, dto, idempotencyKey)` |
| `ReportUsageHandler` | `ReportUsageCommand(agreementId, assetId, dto)` | `UsageRecordApi.create3(agreementId, assetId, dto, idempotencyKey)` |
| `CreateEndOptionHandler` | `CreateEndOptionCommand(agreementId, dto)` | `EndOptionApi.create1(agreementId, dto, idempotencyKey)` |
| `UpdateEndOptionHandler` | `UpdateEndOptionCommand(agreementId, optionId, dto)` | `EndOptionApi.update1(agreementId, optionId, dto, idempotencyKey)` |

### Query Handlers

Each query handler extends `QueryHandler<Q, R>` and is annotated with `@QueryHandlerComponent`. Queries are subject to the 15-minute cache TTL configured on the query bus.

| Handler | Query | Downstream SDK Call |
|---------|-------|---------------------|
| `GetAgreementHandler` | `GetAgreementQuery(agreementId)` | `AssetFinanceAgreementApi.getById(agreementId, null)` |
| `GetAgreementAssetsHandler` | `GetAgreementAssetsQuery(agreementId)` | `AssetFinanceAssetApi.findAll2(agreementId, null, null)` |
| `GetAssetHandler` | `GetAssetQuery(agreementId, assetId)` | `AssetFinanceAssetApi.getById2(agreementId, assetId, null)` |
| `GetAssetDeliveriesHandler` | `GetAssetDeliveriesQuery(agreementId, assetId)` | `DeliveryRecordApi.findAll7(agreementId, assetId, null, null)` |
| `GetAssetPickupsHandler` | `GetAssetPickupsQuery(agreementId, assetId)` | `PickupRecordApi.findAll*(agreementId, assetId, null, null)` |
| `GetAssetReturnsHandler` | `GetAssetReturnsQuery(agreementId, assetId)` | `ReturnRecordApi.findAll*(agreementId, assetId, null, null)` |
| `GetAssetServiceEventsHandler` | `GetAssetServiceEventsQuery(agreementId, assetId)` | `ServiceEventApi.findAll*(agreementId, assetId, null, null)` |
| `GetAssetUsageHandler` | `GetAssetUsageQuery(agreementId, assetId)` | `UsageRecordApi.findAll*(agreementId, assetId, null, null)` |
| `GetAgreementEndOptionsHandler` | `GetAgreementEndOptionsQuery(agreementId)` | `EndOptionApi.findAll1(agreementId, null, null)` |

### SDK Models

Commands and queries carry the following DTOs sourced from `core-lending-asset-finance-sdk`:

- `AssetFinanceAgreementDTO` — agreement header (finance type: `RENTING` or `LEASING`, status, dates, total value, loan servicing case link)
- `AssetFinanceAssetDTO` — physical asset record linked to an agreement
- `DeliveryRecordDTO` — asset delivery event
- `PickupRecordDTO` — asset pickup event
- `ReturnRecordDTO` — asset return with condition assessment
- `ServiceEventDTO` — maintenance, damage, or inspection event
- `UsageRecordDTO` — usage snapshot (mileage, operating hours)
- `EndOptionDTO` — lease-end purchase option
- `PaginationResponse` — paginated wrapper for list results

The `interfaces` module also exposes `AssetFinanceAgreementSummaryDTO`, a lighter projection of agreement data intended for experience-layer consumers that do not need the full core DTO.

## Dependencies

### Upstream (services this service calls)

| Service | Artifact | Purpose |
|---------|----------|---------|
| `core-lending-asset-finance` | `core-lending-asset-finance-sdk` | Persistence and business rules for agreements, assets, and all asset lifecycle events. Default base path: `http://localhost:8090` (override via `ASSET_FINANCE_URL`). |

The infra module instantiates eight WebClient-based SDK API beans through `AssetFinanceClientFactory`:

- `AssetFinanceAgreementApi`
- `AssetFinanceAssetApi`
- `DeliveryRecordApi`
- `PickupRecordApi`
- `ReturnRecordApi`
- `ServiceEventApi`
- `UsageRecordApi`
- `EndOptionApi`

### Downstream (services that consume this service)

Experience-layer services and BFF applications consume this service's REST API. The `domain-lending-asset-finance-sdk` module provides a generated WebClient-based client for those consumers, built from this service's OpenAPI spec using the OpenAPI Generator Maven plugin (`openapi-generator-maven-plugin` 7.0.1, `webclient` library, reactive mode).

### Framework Libraries

| Library | Purpose |
|---------|---------|
| `fireflyframework-starter-domain` | CQRS bus infrastructure (`CommandBus`, `QueryBus`, `@CommandHandlerComponent`, `@QueryHandlerComponent`) |
| `fireflyframework-web` | Global exception handling, error response negotiation |
| `fireflyframework-utils` | Shared utilities |
| `fireflyframework-validators` | Common validation support |
| `spring-boot-starter-webflux` | Reactive HTTP server (Netty) |
| `spring-boot-starter-actuator` | Health, info, and Prometheus metrics endpoints |
| `springdoc-openapi-starter-webflux-ui` | Swagger UI and OpenAPI spec generation |
| `micrometer-registry-prometheus` | Prometheus metrics export |
| `mapstruct` | Compile-time bean mapping |
| `lombok` | Boilerplate reduction |

## Configuration

All configuration is in `domain-lending-asset-finance-web/src/main/resources/application.yaml`.

### Core Settings

| Property | Default | Description |
|----------|---------|-------------|
| `spring.application.name` | `domain-lending-asset-finance` | Service name |
| `spring.application.version` | `1.0.0` | Service version |
| `spring.threads.virtual.enabled` | `true` | Enables Java virtual threads |
| `server.address` | `localhost` (env: `SERVER_ADDRESS`) | Bind address |
| `server.port` | `8043` (env: `SERVER_PORT`) | HTTP listen port |
| `server.shutdown` | `graceful` | Graceful shutdown on SIGTERM |

### Downstream Service URL

| Property | Default | Description |
|----------|---------|-------------|
| `api-configuration.core-lending.asset-finance.base-path` | `http://localhost:8090` (env: `ASSET_FINANCE_URL`) | Base URL of the `core-lending-asset-finance` service. Bound to `AssetFinanceProperties`. |

### CQRS

| Property | Value | Description |
|----------|-------|-------------|
| `firefly.cqrs.enabled` | `true` | Enables the CQRS framework |
| `firefly.cqrs.command.timeout` | `30s` | Maximum duration for command execution |
| `firefly.cqrs.command.metrics-enabled` | `true` | Emit Micrometer metrics for commands |
| `firefly.cqrs.command.tracing-enabled` | `true` | Enable distributed tracing for commands |
| `firefly.cqrs.query.timeout` | `15s` | Maximum duration for query execution |
| `firefly.cqrs.query.caching-enabled` | `true` | Enable query result caching |
| `firefly.cqrs.query.cache-ttl` | `15m` | Query cache time-to-live |
| `firefly.saga.performance.enabled` | `true` | Enable saga performance monitoring |

### Event-Driven Architecture (EDA)

| Property | Value | Description |
|----------|-------|-------------|
| `firefly.eda.enabled` | `true` | Enable event publishing |
| `firefly.eda.default-publisher-type` | `KAFKA` | Default publisher backend |
| `firefly.eda.default-connection-id` | `default` | Connection profile to use |
| `firefly.eda.publishers.kafka.default.enabled` | `true` | Activate the default Kafka publisher |
| `firefly.eda.publishers.kafka.default.default-topic` | `domain-layer` | Kafka topic for domain events |
| `firefly.eda.publishers.kafka.default.bootstrap-servers` | `localhost:9092` | Kafka broker address |
| `firefly.stepevents.enabled` | `true` | Emit CQRS step execution events |

### Actuator / Observability

| Property | Value | Description |
|----------|-------|-------------|
| `management.endpoints.web.exposure.include` | `health,info,prometheus` | Exposed actuator endpoints |
| `management.endpoint.health.show-details` | `when-authorized` | Show health details only to authorized callers |
| `management.health.livenessState.enabled` | `true` | Kubernetes liveness probe support |
| `management.health.readinessState.enabled` | `true` | Kubernetes readiness probe support |

### OpenAPI / Swagger UI

| Property | Value | Description |
|----------|-------|-------------|
| `springdoc.api-docs.path` | `/v3/api-docs` | OpenAPI JSON spec path |
| `springdoc.swagger-ui.path` | `/swagger-ui.html` | Swagger UI path |
| `springdoc.packages-to-scan` | `com.firefly.domain.lending.assetfinance.web.controllers` | Packages scanned for API definitions |
| `springdoc.paths-to-match` | `/api/**` | URL patterns included in the spec |

Swagger UI and the API docs endpoint are **disabled in the `prod` profile**.

### Logging Profiles

| Profile | Root Level | `com.firefly` Level | `org.springframework` Level |
|---------|-----------|---------------------|-----------------------------|
| _(default)_ | — | — | — |
| `dev` | `INFO` | `DEBUG` | — |
| `pre` | `INFO` | `INFO` | — |
| `prod` | `WARN` | `INFO` | `WARN` |

## Running Locally

Ensure `core-lending-asset-finance` is running on `http://localhost:8090` and a Kafka broker is available on `localhost:9092` before starting this service.

```bash
mvn clean install -DskipTests
cd /Users/casanchez/Desktop/firefly-oss/domain-lending-asset-finance
mvn spring-boot:run -pl domain-lending-asset-finance-web
```

To override the downstream service URL or port at startup:

```bash
ASSET_FINANCE_URL=http://my-core-service:8090 SERVER_PORT=8043 mvn spring-boot:run -pl domain-lending-asset-finance-web
```

Server port: **8043**

Swagger UI (non-production): [http://localhost:8043/swagger-ui.html](http://localhost:8043/swagger-ui.html)

Health check: [http://localhost:8043/actuator/health](http://localhost:8043/actuator/health)

Prometheus metrics: [http://localhost:8043/actuator/prometheus](http://localhost:8043/actuator/prometheus)

## Testing

The web layer is tested with `@WebFluxTest` and `WebTestClient` in `AssetFinanceControllerTest`. The `AssetFinanceService` is mocked with Mockito; the `fireflyframework-web` global exception handler beans are also mocked as required by the test slice. Tests cover HTTP status codes for happy paths (201 Created, 200 OK) and not-found scenarios (404) across agreements, assets, and end options.

```bash
mvn clean verify
```

To run only the web module tests:

```bash
mvn clean verify -pl domain-lending-asset-finance-web
```
