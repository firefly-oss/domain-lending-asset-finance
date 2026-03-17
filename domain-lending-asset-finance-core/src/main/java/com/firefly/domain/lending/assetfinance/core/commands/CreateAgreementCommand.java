package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

/**
 * Command to create a new asset finance agreement in core-lending-asset-finance.
 */
public class CreateAgreementCommand implements Command<AssetFinanceAgreementDTO> {

    @NotNull
    private final AssetFinanceAgreementDTO dto;

    /**
     * Creates a new CreateAgreementCommand.
     *
     * @param dto the agreement payload
     */
    public CreateAgreementCommand(AssetFinanceAgreementDTO dto) {
        this.dto = dto;
    }

    /**
     * Returns the agreement payload.
     *
     * @return the agreement DTO
     */
    public AssetFinanceAgreementDTO getDto() {
        return dto;
    }
}
