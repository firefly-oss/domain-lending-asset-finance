package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to retrieve a single asset finance agreement by identifier.
 */
public class GetAgreementQuery implements Query<AssetFinanceAgreementDTO> {

    @NotNull
    private final UUID agreementId;

    /**
     * Creates a new GetAgreementQuery.
     *
     * @param agreementId the unique identifier of the agreement to fetch
     */
    public GetAgreementQuery(UUID agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * Returns the agreement identifier.
     *
     * @return the agreement ID
     */
    public UUID getAgreementId() {
        return agreementId;
    }
}
