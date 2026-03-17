package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.ReturnRecordDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to register a return record and condition assessment for an asset.
 */
public class RegisterReturnCommand implements Command<ReturnRecordDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID assetId;

    @NotNull
    private final ReturnRecordDTO dto;

    /**
     * Creates a new RegisterReturnCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the return record payload
     */
    public RegisterReturnCommand(UUID agreementId, UUID assetId, ReturnRecordDTO dto) {
        this.agreementId = agreementId;
        this.assetId = assetId;
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
     * Returns the asset identifier.
     *
     * @return the asset ID
     */
    public UUID getAssetId() {
        return assetId;
    }

    /**
     * Returns the return record payload.
     *
     * @return the return record DTO
     */
    public ReturnRecordDTO getDto() {
        return dto;
    }
}
