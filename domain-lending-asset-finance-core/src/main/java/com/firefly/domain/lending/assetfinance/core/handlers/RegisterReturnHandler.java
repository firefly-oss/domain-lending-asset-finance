package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.ReturnRecordApi;
import com.firefly.core.lending.assetfinance.sdk.model.ReturnRecordDTO;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterReturnCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link RegisterReturnCommand} by creating a return record in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class RegisterReturnHandler extends CommandHandler<RegisterReturnCommand, ReturnRecordDTO> {

    private final ReturnRecordApi returnApi;

    /**
     * Creates a new RegisterReturnHandler.
     *
     * @param returnApi the SDK client for return record operations
     */
    public RegisterReturnHandler(ReturnRecordApi returnApi) {
        this.returnApi = returnApi;
    }

    @Override
    protected Mono<ReturnRecordDTO> doHandle(RegisterReturnCommand command) {
        log.debug("Registering return for asset: agreementId={}, assetId={}", command.getAgreementId(), command.getAssetId());
        return returnApi.create5(command.getAgreementId(), command.getAssetId(), command.getDto(), UUID.randomUUID().toString());
    }
}
