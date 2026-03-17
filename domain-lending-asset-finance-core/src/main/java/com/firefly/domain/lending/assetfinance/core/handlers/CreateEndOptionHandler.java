package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.EndOptionApi;
import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import com.firefly.domain.lending.assetfinance.core.commands.CreateEndOptionCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link CreateEndOptionCommand} by creating a lease-end option in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class CreateEndOptionHandler extends CommandHandler<CreateEndOptionCommand, EndOptionDTO> {

    private final EndOptionApi endOptionApi;

    /**
     * Creates a new CreateEndOptionHandler.
     *
     * @param endOptionApi the SDK client for end option operations
     */
    public CreateEndOptionHandler(EndOptionApi endOptionApi) {
        this.endOptionApi = endOptionApi;
    }

    @Override
    protected Mono<EndOptionDTO> doHandle(CreateEndOptionCommand command) {
        log.debug("Creating end option for agreement: agreementId={}", command.getAgreementId());
        return endOptionApi.create1(command.getAgreementId(), command.getDto(), UUID.randomUUID().toString());
    }
}
