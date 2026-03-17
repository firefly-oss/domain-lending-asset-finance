package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to create a lease-end purchase option for an agreement.
 */
public class CreateEndOptionCommand implements Command<EndOptionDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final EndOptionDTO dto;

    /**
     * Creates a new CreateEndOptionCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param dto         the end option payload
     */
    public CreateEndOptionCommand(UUID agreementId, EndOptionDTO dto) {
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
     * Returns the end option payload.
     *
     * @return the end option DTO
     */
    public EndOptionDTO getDto() {
        return dto;
    }
}
