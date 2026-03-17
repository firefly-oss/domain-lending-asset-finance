package com.firefly.domain.lending.assetfinance.web.controllers;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import com.firefly.core.lending.assetfinance.sdk.model.DeliveryRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.core.lending.assetfinance.sdk.model.PickupRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.ReturnRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.ServiceEventDTO;
import com.firefly.core.lending.assetfinance.sdk.model.UsageRecordDTO;
import com.firefly.domain.lending.assetfinance.core.services.AssetFinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller exposing the asset finance domain API.
 *
 * <p>All endpoints are fully reactive ({@code Mono}) and follow the platform convention
 * of using UUID path variables for resource identification.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/asset-finance")
@Tag(name = "Asset Finance", description = "Domain-level orchestration for asset finance workflows (renting and leasing)")
public class AssetFinanceController {

    private final AssetFinanceService assetFinanceService;

    // ── Agreements ──────────────────────────────────────────────────────────

    /**
     * Creates a new asset finance agreement.
     *
     * @param dto the agreement payload
     * @return 201 Created with the new agreement, or 400 on invalid input
     */
    @Operation(summary = "Create agreement", description = "Creates a new asset finance agreement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agreement created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAgreementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping(value = "/agreements", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> createAgreement(
            @RequestBody AssetFinanceAgreementDTO dto) {
        return assetFinanceService.createAgreement(dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    /**
     * Retrieves an asset finance agreement by its identifier.
     *
     * @param agreementId the unique identifier of the agreement
     * @return 200 with the agreement, or 404 if not found
     */
    @Operation(summary = "Get agreement", description = "Retrieves an asset finance agreement by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agreement found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAgreementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content)
    })
    @GetMapping(value = "/agreements/{agreementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> getAgreement(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId) {
        return assetFinanceService.getAgreement(agreementId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * Updates an existing asset finance agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the updated agreement payload
     * @return 200 with the updated agreement, or 404 if not found
     */
    @Operation(summary = "Update agreement", description = "Updates an existing asset finance agreement.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agreement updated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAgreementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content)
    })
    @PutMapping(value = "/agreements/{agreementId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> updateAgreement(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId,
            @RequestBody AssetFinanceAgreementDTO dto) {
        return assetFinanceService.updateAgreement(agreementId, dto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    // ── Assets ───────────────────────────────────────────────────────────────

    /**
     * Lists all physical assets registered under an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @return 200 with a paginated list of assets
     */
    @Operation(summary = "List assets", description = "Lists all physical assets registered under an agreement.")
    @ApiResponse(responseCode = "200", description = "Assets listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponse.class)))
    @GetMapping(value = "/agreements/{agreementId}/assets", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse>> getAgreementAssets(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId) {
        return assetFinanceService.getAgreementAssets(agreementId)
                .map(ResponseEntity::ok);
    }

    /**
     * Registers a physical asset under an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the asset payload
     * @return 201 Created with the registered asset, or 400 on invalid input
     */
    @Operation(summary = "Register asset", description = "Registers a physical asset under an agreement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asset registered",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAssetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping(value = "/agreements/{agreementId}/assets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> registerAsset(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId,
            @RequestBody AssetFinanceAssetDTO dto) {
        return assetFinanceService.registerAsset(agreementId, dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    /**
     * Retrieves a single asset by its identifier.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return 200 with the asset, or 404 if not found
     */
    @Operation(summary = "Get asset", description = "Retrieves a single asset by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asset found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAssetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @GetMapping(value = "/agreements/{agreementId}/assets/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> getAsset(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable UUID assetId) {
        return assetFinanceService.getAsset(agreementId, assetId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    // ── Deliveries ───────────────────────────────────────────────────────────

    /**
     * Lists all delivery records for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return 200 with a paginated list of delivery records
     */
    @Operation(summary = "List deliveries", description = "Lists all delivery records for an asset.")
    @ApiResponse(responseCode = "200", description = "Deliveries listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponse.class)))
    @GetMapping(value = "/agreements/{agreementId}/assets/{assetId}/deliveries", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse>> getAssetDeliveries(
            @PathVariable UUID agreementId, @PathVariable UUID assetId) {
        return assetFinanceService.getAssetDeliveries(agreementId, assetId)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a delivery record for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the delivery record payload
     * @return 201 Created with the new delivery record
     */
    @Operation(summary = "Register delivery", description = "Creates a delivery record for an asset.")
    @ApiResponse(responseCode = "201", description = "Delivery registered",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DeliveryRecordDTO.class)))
    @PostMapping(value = "/agreements/{agreementId}/assets/{assetId}/deliveries", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DeliveryRecordDTO>> registerDelivery(
            @PathVariable UUID agreementId, @PathVariable UUID assetId,
            @RequestBody DeliveryRecordDTO dto) {
        return assetFinanceService.registerDelivery(agreementId, assetId, dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    // ── Pickups ──────────────────────────────────────────────────────────────

    /**
     * Lists all pickup records for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return 200 with a paginated list of pickup records
     */
    @Operation(summary = "List pickups", description = "Lists all pickup records for an asset.")
    @ApiResponse(responseCode = "200", description = "Pickups listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponse.class)))
    @GetMapping(value = "/agreements/{agreementId}/assets/{assetId}/pickups", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse>> getAssetPickups(
            @PathVariable UUID agreementId, @PathVariable UUID assetId) {
        return assetFinanceService.getAssetPickups(agreementId, assetId)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a pickup record for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the pickup record payload
     * @return 201 Created with the new pickup record
     */
    @Operation(summary = "Register pickup", description = "Creates a pickup record for an asset.")
    @ApiResponse(responseCode = "201", description = "Pickup registered",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PickupRecordDTO.class)))
    @PostMapping(value = "/agreements/{agreementId}/assets/{assetId}/pickups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PickupRecordDTO>> registerPickup(
            @PathVariable UUID agreementId, @PathVariable UUID assetId,
            @RequestBody PickupRecordDTO dto) {
        return assetFinanceService.registerPickup(agreementId, assetId, dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    // ── Returns ──────────────────────────────────────────────────────────────

    /**
     * Lists all return records for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return 200 with a paginated list of return records
     */
    @Operation(summary = "List returns", description = "Lists all return records for an asset.")
    @ApiResponse(responseCode = "200", description = "Returns listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponse.class)))
    @GetMapping(value = "/agreements/{agreementId}/assets/{assetId}/returns", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse>> getAssetReturns(
            @PathVariable UUID agreementId, @PathVariable UUID assetId) {
        return assetFinanceService.getAssetReturns(agreementId, assetId)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a return and condition assessment record for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the return record payload
     * @return 201 Created with the new return record
     */
    @Operation(summary = "Register return", description = "Creates a return and condition assessment record for an asset.")
    @ApiResponse(responseCode = "201", description = "Return registered",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ReturnRecordDTO.class)))
    @PostMapping(value = "/agreements/{agreementId}/assets/{assetId}/returns", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ReturnRecordDTO>> registerReturn(
            @PathVariable UUID agreementId, @PathVariable UUID assetId,
            @RequestBody ReturnRecordDTO dto) {
        return assetFinanceService.registerReturn(agreementId, assetId, dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    // ── Service Events ───────────────────────────────────────────────────────

    /**
     * Lists all service events logged for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return 200 with a paginated list of service events
     */
    @Operation(summary = "List service events", description = "Lists all service events logged for an asset.")
    @ApiResponse(responseCode = "200", description = "Events listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponse.class)))
    @GetMapping(value = "/agreements/{agreementId}/assets/{assetId}/service-events", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse>> getAssetServiceEvents(
            @PathVariable UUID agreementId, @PathVariable UUID assetId) {
        return assetFinanceService.getAssetServiceEvents(agreementId, assetId)
                .map(ResponseEntity::ok);
    }

    /**
     * Logs a service event (maintenance, damage, inspection) for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the service event payload
     * @return 201 Created with the new service event
     */
    @Operation(summary = "Register service event", description = "Logs a service event (maintenance, damage, inspection) for an asset.")
    @ApiResponse(responseCode = "201", description = "Event registered",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ServiceEventDTO.class)))
    @PostMapping(value = "/agreements/{agreementId}/assets/{assetId}/service-events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ServiceEventDTO>> registerServiceEvent(
            @PathVariable UUID agreementId, @PathVariable UUID assetId,
            @RequestBody ServiceEventDTO dto) {
        return assetFinanceService.registerServiceEvent(agreementId, assetId, dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    // ── Usage ────────────────────────────────────────────────────────────────

    /**
     * Lists all usage snapshots for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return 200 with a paginated list of usage records
     */
    @Operation(summary = "List usage records", description = "Lists all usage snapshots for an asset.")
    @ApiResponse(responseCode = "200", description = "Usage records listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponse.class)))
    @GetMapping(value = "/agreements/{agreementId}/assets/{assetId}/usage", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse>> getAssetUsage(
            @PathVariable UUID agreementId, @PathVariable UUID assetId) {
        return assetFinanceService.getAssetUsage(agreementId, assetId)
                .map(ResponseEntity::ok);
    }

    /**
     * Records a usage snapshot (mileage, operating hours) for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the usage record payload
     * @return 201 Created with the new usage record
     */
    @Operation(summary = "Report usage", description = "Records a usage snapshot (mileage, operating hours) for an asset.")
    @ApiResponse(responseCode = "201", description = "Usage reported",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsageRecordDTO.class)))
    @PostMapping(value = "/agreements/{agreementId}/assets/{assetId}/usage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<UsageRecordDTO>> reportUsage(
            @PathVariable UUID agreementId, @PathVariable UUID assetId,
            @RequestBody UsageRecordDTO dto) {
        return assetFinanceService.reportUsage(agreementId, assetId, dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    // ── End Options ──────────────────────────────────────────────────────────

    /**
     * Lists all lease-end purchase options for an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @return 200 with a paginated list of end options
     */
    @Operation(summary = "List end options", description = "Lists all lease-end purchase options for an agreement.")
    @ApiResponse(responseCode = "200", description = "End options listed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PaginationResponse.class)))
    @GetMapping(value = "/agreements/{agreementId}/end-options", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse>> getAgreementEndOptions(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId) {
        return assetFinanceService.getAgreementEndOptions(agreementId)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a lease-end purchase option for an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the end option payload
     * @return 201 Created with the new end option
     */
    @Operation(summary = "Create end option", description = "Creates a lease-end purchase option for an agreement.")
    @ApiResponse(responseCode = "201", description = "End option created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EndOptionDTO.class)))
    @PostMapping(value = "/agreements/{agreementId}/end-options", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<EndOptionDTO>> createEndOption(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId,
            @RequestBody EndOptionDTO dto) {
        return assetFinanceService.createEndOption(agreementId, dto)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result));
    }

    /**
     * Updates a lease-end option (e.g. marks it as exercised).
     *
     * @param agreementId the unique identifier of the agreement
     * @param optionId    the unique identifier of the end option
     * @param dto         the updated end option payload
     * @return 200 with the updated end option, or 404 if not found
     */
    @Operation(summary = "Update end option", description = "Updates a lease-end option (e.g. marks it as exercised).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "End option updated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EndOptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "End option not found", content = @Content)
    })
    @PutMapping(value = "/agreements/{agreementId}/end-options/{optionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<EndOptionDTO>> updateEndOption(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable UUID agreementId,
            @Parameter(description = "Unique identifier of the end option", required = true)
            @PathVariable UUID optionId,
            @RequestBody EndOptionDTO dto) {
        return assetFinanceService.updateEndOption(agreementId, optionId, dto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
