/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.domain.lending.assetfinance.core.services;

import io.fireflysoftware.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.DeliveryRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.EndOptionDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseAssetFinanceAgreementDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseAssetFinanceAssetDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseDeliveryRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponsePickupRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseReturnRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseServiceEventDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseUsageRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseEndOptionDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PickupRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.ReturnRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.ServiceEventDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.UsageRecordDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.CreateAgreementCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.CreateEndOptionCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAgreementAssetsQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.ListAgreementsQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAgreementEndOptionsQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAgreementQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetDeliveriesQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetPickupsQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetReturnsQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetServiceEventsQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetUsageQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.RegisterAssetCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.RegisterDeliveryCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.RegisterPickupCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.RegisterReturnCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.RegisterServiceEventCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.ReportUsageCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateAgreementCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateAgreementStatusCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateEndOptionCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants;
import io.fireflysoftware.domain.lending.assetfinance.core.lifecycle.AssetFinanceAgreementLifecycle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import org.fireflyframework.orchestration.saga.engine.SagaEngine;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.fireflyframework.orchestration.saga.engine.StepInputs;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Default implementation of {@link AssetFinanceService}.
 *
 * <p>Acts as a thin dispatcher: creates the appropriate Command or Query and sends it
 * via the CQRS bus, leaving all SDK interactions to the individual handlers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssetFinanceServiceImpl implements AssetFinanceService {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final SagaEngine sagaEngine;

    // ── Agreements ──────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseAssetFinanceAgreementDTO> listAgreements() {
        return queryBus.query(new ListAgreementsQuery());
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> createAgreement(AssetFinanceAgreementDTO dto) {
        if (dto == null || dto.getFinanceType() == null) {
            return Mono.error(new IllegalArgumentException("Finance type is required"));
        }
        StepInputs inputs = StepInputs.builder()
                .forStepId(AssetFinanceAgreementSagaConstants.STEP_REGISTER, new CreateAgreementCommand(dto))
                .build();
        return sagaEngine.execute(AssetFinanceAgreementSagaConstants.SAGA_REGISTER, inputs)
                .flatMap(result -> outcome(result, AssetFinanceAgreementSagaConstants.STEP_REGISTER, AssetFinanceAgreementDTO.class));
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> getAgreement(UUID agreementId) {
        return queryBus.query(new GetAgreementQuery(agreementId));
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> updateAgreement(UUID agreementId, AssetFinanceAgreementDTO dto) {
        StepInputs inputs = StepInputs.builder()
                .forStepId(AssetFinanceAgreementSagaConstants.STEP_RETRIEVE_FOR_UPDATE, new GetAgreementQuery(agreementId))
                .forStepId(AssetFinanceAgreementSagaConstants.STEP_UPDATE_RECORD, new UpdateAgreementCommand(agreementId, dto))
                .build();
        return sagaEngine.execute(AssetFinanceAgreementSagaConstants.SAGA_UPDATE_RECORD, inputs)
                .flatMap(result -> outcome(result, AssetFinanceAgreementSagaConstants.STEP_UPDATE_RECORD, AssetFinanceAgreementDTO.class));
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> updateAgreementStatus(UUID agreementId, String status, String statusReason) {
        StepInputs inputs = StepInputs.builder()
                .forStepId(AssetFinanceAgreementSagaConstants.STEP_RETRIEVE, new GetAgreementQuery(agreementId))
                .forStepId(AssetFinanceAgreementSagaConstants.STEP_UPDATE_STATUS, new UpdateAgreementStatusCommand(agreementId, status, statusReason))
                .build();
        return sagaEngine.execute(AssetFinanceAgreementSagaConstants.SAGA_UPDATE_STATUS, inputs)
                .flatMap(result -> outcome(result, AssetFinanceAgreementSagaConstants.STEP_UPDATE_STATUS, AssetFinanceAgreementDTO.class));
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> closeAgreement(UUID agreementId, String statusReason) {
        return getAgreement(agreementId)
                .switchIfEmpty(Mono.error(new IllegalStateException("Asset finance agreement was not found")))
                .flatMap(current -> {
                    if (!AssetFinanceAgreementLifecycle.canTransition(
                            current.getAgreementStatus(), AssetFinanceAgreementDTO.AgreementStatusEnum.CLOSED)) {
                        String from = current.getAgreementStatus() == null
                                ? "unset"
                                : current.getAgreementStatus().getValue();
                        return Mono.<AssetFinanceAgreementDTO>error(new IllegalStateException(
                                "Asset finance agreement " + agreementId + " cannot move from " + from + " to CLOSED"));
                    }
                    return updateAgreementStatus(agreementId, AssetFinanceAgreementDTO.AgreementStatusEnum.CLOSED.getValue(), statusReason);
                });
    }

    private static <T> Mono<T> outcome(SagaResult result, String stepId, Class<T> type) {
        if (result.isFailed()) {
            Throwable error = result.error().orElseGet(() ->
                    new IllegalStateException("Saga " + result.sagaName() + " failed"));
            return Mono.error(error);
        }
        return Mono.justOrEmpty(result.resultOf(stepId, type))
                .switchIfEmpty(Mono.error(new IllegalStateException("Saga step " + stepId + " produced no result")));
    }

    // ── Assets ───────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseAssetFinanceAssetDTO> getAgreementAssets(UUID agreementId) {
        return queryBus.query(new GetAgreementAssetsQuery(agreementId));
    }

    @Override
    public Mono<AssetFinanceAssetDTO> registerAsset(UUID agreementId, AssetFinanceAssetDTO dto) {
        return commandBus.send(new RegisterAssetCommand(agreementId, dto));
    }

    @Override
    public Mono<AssetFinanceAssetDTO> getAsset(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetQuery(agreementId, assetId));
    }

    // ── Deliveries ───────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseDeliveryRecordDTO> getAssetDeliveries(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetDeliveriesQuery(agreementId, assetId));
    }

    @Override
    public Mono<DeliveryRecordDTO> registerDelivery(UUID agreementId, UUID assetId, DeliveryRecordDTO dto) {
        return commandBus.send(new RegisterDeliveryCommand(agreementId, assetId, dto));
    }

    // ── Pickups ──────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponsePickupRecordDTO> getAssetPickups(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetPickupsQuery(agreementId, assetId));
    }

    @Override
    public Mono<PickupRecordDTO> registerPickup(UUID agreementId, UUID assetId, PickupRecordDTO dto) {
        return commandBus.send(new RegisterPickupCommand(agreementId, assetId, dto));
    }

    // ── Returns ──────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseReturnRecordDTO> getAssetReturns(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetReturnsQuery(agreementId, assetId));
    }

    @Override
    public Mono<ReturnRecordDTO> registerReturn(UUID agreementId, UUID assetId, ReturnRecordDTO dto) {
        return commandBus.send(new RegisterReturnCommand(agreementId, assetId, dto));
    }

    // ── Service Events ───────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseServiceEventDTO> getAssetServiceEvents(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetServiceEventsQuery(agreementId, assetId));
    }

    @Override
    public Mono<ServiceEventDTO> registerServiceEvent(UUID agreementId, UUID assetId, ServiceEventDTO dto) {
        return commandBus.send(new RegisterServiceEventCommand(agreementId, assetId, dto));
    }

    // ── Usage ────────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseUsageRecordDTO> getAssetUsage(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetUsageQuery(agreementId, assetId));
    }

    @Override
    public Mono<UsageRecordDTO> reportUsage(UUID agreementId, UUID assetId, UsageRecordDTO dto) {
        return commandBus.send(new ReportUsageCommand(agreementId, assetId, dto));
    }

    // ── End Options ──────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseEndOptionDTO> getAgreementEndOptions(UUID agreementId) {
        return queryBus.query(new GetAgreementEndOptionsQuery(agreementId));
    }

    @Override
    public Mono<EndOptionDTO> createEndOption(UUID agreementId, EndOptionDTO dto) {
        return commandBus.send(new CreateEndOptionCommand(agreementId, dto));
    }

    @Override
    public Mono<EndOptionDTO> updateEndOption(UUID agreementId, UUID optionId, EndOptionDTO dto) {
        return commandBus.send(new UpdateEndOptionCommand(agreementId, optionId, dto));
    }
}
