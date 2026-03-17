package com.firefly.domain.lending.assetfinance.core.services;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import com.firefly.core.lending.assetfinance.sdk.model.DeliveryRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.core.lending.assetfinance.sdk.model.PickupRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.ReturnRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.ServiceEventDTO;
import com.firefly.core.lending.assetfinance.sdk.model.UsageRecordDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Domain service interface for asset finance orchestration.
 *
 * <p>Coordinates calls to {@code core-lending-asset-finance} via CQRS commands and queries,
 * exposing a domain-level API for experience-layer consumers.
 */
public interface AssetFinanceService {

    // ── Agreements ──────────────────────────────────────────────────────────

    /**
     * Creates a new asset finance agreement.
     *
     * @param dto the agreement payload
     * @return the created agreement
     */
    Mono<AssetFinanceAgreementDTO> createAgreement(AssetFinanceAgreementDTO dto);

    /**
     * Retrieves an asset finance agreement by its identifier.
     *
     * @param agreementId the unique identifier of the agreement
     * @return the agreement, or empty if not found
     */
    Mono<AssetFinanceAgreementDTO> getAgreement(UUID agreementId);

    /**
     * Updates an existing asset finance agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the updated agreement payload
     * @return the updated agreement
     */
    Mono<AssetFinanceAgreementDTO> updateAgreement(UUID agreementId, AssetFinanceAgreementDTO dto);

    // ── Assets ───────────────────────────────────────────────────────────────

    /**
     * Lists all physical assets registered under an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @return a paginated list of assets
     */
    Mono<PaginationResponse> getAgreementAssets(UUID agreementId);

    /**
     * Registers a physical asset under an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the asset payload
     * @return the registered asset
     */
    Mono<AssetFinanceAssetDTO> registerAsset(UUID agreementId, AssetFinanceAssetDTO dto);

    /**
     * Retrieves a single asset by its identifier within an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return the asset, or empty if not found
     */
    Mono<AssetFinanceAssetDTO> getAsset(UUID agreementId, UUID assetId);

    // ── Deliveries ───────────────────────────────────────────────────────────

    /**
     * Lists all delivery records for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return a paginated list of delivery records
     */
    Mono<PaginationResponse> getAssetDeliveries(UUID agreementId, UUID assetId);

    /**
     * Creates a delivery record for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the delivery record payload
     * @return the created delivery record
     */
    Mono<DeliveryRecordDTO> registerDelivery(UUID agreementId, UUID assetId, DeliveryRecordDTO dto);

    // ── Pickups ──────────────────────────────────────────────────────────────

    /**
     * Lists all pickup records for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return a paginated list of pickup records
     */
    Mono<PaginationResponse> getAssetPickups(UUID agreementId, UUID assetId);

    /**
     * Creates a pickup record for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the pickup record payload
     * @return the created pickup record
     */
    Mono<PickupRecordDTO> registerPickup(UUID agreementId, UUID assetId, PickupRecordDTO dto);

    // ── Returns ──────────────────────────────────────────────────────────────

    /**
     * Lists all return records for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return a paginated list of return records
     */
    Mono<PaginationResponse> getAssetReturns(UUID agreementId, UUID assetId);

    /**
     * Creates a return and condition assessment record for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the return record payload
     * @return the created return record
     */
    Mono<ReturnRecordDTO> registerReturn(UUID agreementId, UUID assetId, ReturnRecordDTO dto);

    // ── Service Events ───────────────────────────────────────────────────────

    /**
     * Lists all service events logged for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return a paginated list of service events
     */
    Mono<PaginationResponse> getAssetServiceEvents(UUID agreementId, UUID assetId);

    /**
     * Logs a service event (maintenance, damage, inspection) for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the service event payload
     * @return the created service event
     */
    Mono<ServiceEventDTO> registerServiceEvent(UUID agreementId, UUID assetId, ServiceEventDTO dto);

    // ── Usage ────────────────────────────────────────────────────────────────

    /**
     * Lists all usage snapshots for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @return a paginated list of usage records
     */
    Mono<PaginationResponse> getAssetUsage(UUID agreementId, UUID assetId);

    /**
     * Records a usage snapshot (mileage, operating hours) for an asset.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the usage record payload
     * @return the created usage record
     */
    Mono<UsageRecordDTO> reportUsage(UUID agreementId, UUID assetId, UsageRecordDTO dto);

    // ── End Options ──────────────────────────────────────────────────────────

    /**
     * Lists all lease-end purchase options for an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @return a paginated list of end options
     */
    Mono<PaginationResponse> getAgreementEndOptions(UUID agreementId);

    /**
     * Creates a lease-end purchase option for an agreement.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the end option payload
     * @return the created end option
     */
    Mono<EndOptionDTO> createEndOption(UUID agreementId, EndOptionDTO dto);

    /**
     * Updates a lease-end option (e.g. marks it as exercised).
     *
     * @param agreementId the unique identifier of the agreement
     * @param optionId    the unique identifier of the end option
     * @param dto         the updated end option payload
     * @return the updated end option
     */
    Mono<EndOptionDTO> updateEndOption(UUID agreementId, UUID optionId, EndOptionDTO dto);
}
