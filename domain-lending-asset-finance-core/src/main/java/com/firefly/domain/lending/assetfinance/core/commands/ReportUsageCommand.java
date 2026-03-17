package com.firefly.domain.lending.assetfinance.core.commands;

import com.firefly.core.lending.assetfinance.sdk.model.UsageRecordDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to report a usage snapshot (mileage, operating hours) for an asset.
 */
public class ReportUsageCommand implements Command<UsageRecordDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final UUID assetId;

    @NotNull
    private final UsageRecordDTO dto;

    /**
     * Creates a new ReportUsageCommand.
     *
     * @param agreementId the unique identifier of the agreement
     * @param assetId     the unique identifier of the asset
     * @param dto         the usage record payload
     */
    public ReportUsageCommand(UUID agreementId, UUID assetId, UsageRecordDTO dto) {
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
     * Returns the usage record payload.
     *
     * @return the usage record DTO
     */
    public UsageRecordDTO getDto() {
        return dto;
    }
}
