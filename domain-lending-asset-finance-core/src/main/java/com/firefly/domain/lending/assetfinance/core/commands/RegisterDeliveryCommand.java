package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.DeliveryRecordDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to register a delivery record for an asset.
 */
public class RegisterDeliveryCommand implements Command<DeliveryRecordDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID assetId;

    @NotNull
    private final DeliveryRecordDTO dto;

    /**
     * Creates a new RegisterDeliveryCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the delivery record payload
     */
    public RegisterDeliveryCommand(UUID agreementId, UUID assetId, DeliveryRecordDTO dto) {
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
     * Returns the delivery record payload.
     *
     * @return the delivery record DTO
     */
    public DeliveryRecordDTO getDto() {
        return dto;
    }
}
