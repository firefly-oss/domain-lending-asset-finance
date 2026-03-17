package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.PickupRecordDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to register a pickup record for an asset.
 */
public class RegisterPickupCommand implements Command<PickupRecordDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID assetId;

    @NotNull
    private final PickupRecordDTO dto;

    /**
     * Creates a new RegisterPickupCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the pickup record payload
     */
    public RegisterPickupCommand(UUID agreementId, UUID assetId, PickupRecordDTO dto) {
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
     * Returns the pickup record payload.
     *
     * @return the pickup record DTO
     */
    public PickupRecordDTO getDto() {
        return dto;
    }
}
