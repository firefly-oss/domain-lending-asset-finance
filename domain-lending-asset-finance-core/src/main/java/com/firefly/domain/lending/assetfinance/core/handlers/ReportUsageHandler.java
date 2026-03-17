package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.UsageRecordApi;
import com.firefly.core.lending.assetfinance.sdk.model.UsageRecordDTO;
import com.firefly.domain.lending.assetfinance.core.commands.ReportUsageCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link ReportUsageCommand} by recording a usage snapshot in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class ReportUsageHandler extends CommandHandler<ReportUsageCommand, UsageRecordDTO> {

    private final UsageRecordApi usageApi;

    /**
     * Creates a new ReportUsageHandler.
     *
     * @param usageApi the SDK client for usage record operations
     */
    public ReportUsageHandler(UsageRecordApi usageApi) {
        this.usageApi = usageApi;
    }

    @Override
    protected Mono<UsageRecordDTO> doHandle(ReportUsageCommand command) {
        log.debug("Reporting usage for asset: agreementId={}, assetId={}", command.getAgreementId(), command.getAssetId());
        return usageApi.create3(command.getAgreementId(), command.getAssetId(), command.getDto(), UUID.randomUUID().toString());
    }
}
