package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to update an existing asset finance agreement.
 */
public class UpdateAgreementCommand implements Command<AssetFinanceAgreementDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final AssetFinanceAgreementDTO dto;

    /**
     * Creates a new UpdateAgreementCommand.
     *
     * @param agreementId the unique identifier of the agreement to update
     * @param dto         the updated agreement payload
     */
    public UpdateAgreementCommand(UUID agreementId, AssetFinanceAgreementDTO dto) {
        this.agreementId = agreementId;
        this.dto = dto;
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
     * Returns the updated agreement payload.
     *
     * @return the agreement DTO
     */
    public AssetFinanceAgreementDTO getDto() {
        return dto;
    }
}
