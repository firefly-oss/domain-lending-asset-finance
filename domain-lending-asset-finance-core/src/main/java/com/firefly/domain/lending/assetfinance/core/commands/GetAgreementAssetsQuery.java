package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to list all physical assets registered under an agreement.
 */
public class GetAgreementAssetsQuery implements Query<PaginationResponse> {

    @NotNull
    private final UUID agreementId;

    /**
     * Creates a new GetAgreementAssetsQuery.
     *
     * @param agreementId the unique identifier of the agreement
     */
    public GetAgreementAssetsQuery(UUID agreementId) {
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
