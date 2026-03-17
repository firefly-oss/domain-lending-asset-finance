package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to retrieve a single asset by its identifier within an agreement.
 */
public class GetAssetQuery implements Query<AssetFinanceAssetDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID assetId;

    /**
     * Creates a new GetAssetQuery.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     */
    public GetAssetQuery(UUID agreementId, UUID assetId) {
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
