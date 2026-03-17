package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.ServiceEventDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to log a service event (maintenance, damage, inspection, etc.) against an asset.
 */
public class RegisterServiceEventCommand implements Command<ServiceEventDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID assetId;

    @NotNull
    private final ServiceEventDTO dto;

    /**
     * Creates a new RegisterServiceEventCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the service event payload
     */
    public RegisterServiceEventCommand(UUID agreementId, UUID assetId, ServiceEventDTO dto) {
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
     * Returns the service event payload.
     *
     * @return the service event DTO
     */
    public ServiceEventDTO getDto() {
        return dto;
    }
}
