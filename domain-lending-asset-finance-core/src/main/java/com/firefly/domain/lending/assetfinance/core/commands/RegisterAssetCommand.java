package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to register a physical asset under an agreement.
 */
public class RegisterAssetCommand implements Command<AssetFinanceAssetDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final AssetFinanceAssetDTO dto;

    /**
     * Creates a new RegisterAssetCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the asset payload
     */
    public RegisterAssetCommand(UUID agreementId, AssetFinanceAssetDTO dto) {
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
     * Returns the asset payload.
     *
     * @return the asset DTO
     */
    public AssetFinanceAssetDTO getDto() {
        return dto;
    }
}
