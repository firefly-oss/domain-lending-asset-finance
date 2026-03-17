package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to list all lease-end purchase options for an agreement.
 */
public class GetAgreementEndOptionsQuery implements Query<PaginationResponse> {

    @NotNull
    private final UUID agreementId;

    /**
     * Creates a new GetAgreementEndOptionsQuery.
     *
     * @param agreementId the unique identifier of the agreement
     */
    public GetAgreementEndOptionsQuery(UUID agreementId) {
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
