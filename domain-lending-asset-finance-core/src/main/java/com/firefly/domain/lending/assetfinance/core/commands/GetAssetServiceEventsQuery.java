package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to list all service events logged against an asset.
 */
public class GetAssetServiceEventsQuery implements Query<PaginationResponse> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID assetId;

    /**
     * Creates a new GetAssetServiceEventsQuery.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     */
    public GetAssetServiceEventsQuery(UUID agreementId, UUID assetId) {
        this.agreementId = agreementId;
        this.assetId = assetId;
    }

    /**
     * Returns the agreement identifier.
     *
     * @return the agreement ID
     */
    public UUID getAgreementId() {
        return agreementId;
    }

    /**
     * Returns the asset identifier.
     *
     * @return the asset ID
     */
    public UUID getAssetId() {
        return assetId;
    }
}
