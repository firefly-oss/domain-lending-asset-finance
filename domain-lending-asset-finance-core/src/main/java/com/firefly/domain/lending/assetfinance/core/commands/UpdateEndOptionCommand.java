package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to update an existing lease-end option (e.g. marking it as exercised).
 */
public class UpdateEndOptionCommand implements Command<EndOptionDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID optionId;

    @NotNull
    private final EndOptionDTO dto;

    /**
     * Creates a new UpdateEndOptionCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param optionId    the unique identifier of the end option to update
     * @param dto         the updated end option payload
     */
    public UpdateEndOptionCommand(UUID agreementId, UUID optionId, EndOptionDTO dto) {
        this.agreementId = agreementId;
        this.optionId = optionId;
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
     * Returns the end option identifier.
     *
     * @return the end option ID
     */
    public UUID getOptionId() {
        return optionId;
    }

    /**
     * Returns the updated end option payload.
     *
     * @return the end option DTO
     */
    public EndOptionDTO getDto() {
        return dto;
    }
}
