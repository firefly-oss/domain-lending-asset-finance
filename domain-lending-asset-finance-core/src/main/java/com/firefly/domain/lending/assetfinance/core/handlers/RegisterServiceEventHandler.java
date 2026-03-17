package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.ServiceEventApi;
import com.firefly.core.lending.assetfinance.sdk.model.ServiceEventDTO;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterServiceEventCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link RegisterServiceEventCommand} by logging a service event in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class RegisterServiceEventHandler extends CommandHandler<RegisterServiceEventCommand, ServiceEventDTO> {

    private final ServiceEventApi serviceEventApi;

    /**
     * Creates a new RegisterServiceEventHandler.
     *
     * @param serviceEventApi the SDK client for service event operations
     */
    public RegisterServiceEventHandler(ServiceEventApi serviceEventApi) {
        this.serviceEventApi = serviceEventApi;
    }

    @Override
    protected Mono<ServiceEventDTO> doHandle(RegisterServiceEventCommand command) {
        log.debug("Registering service event for asset: agreementId={}, assetId={}", command.getAgreementId(), command.getAssetId());
        return serviceEventApi.create4(command.getAgreementId(), command.getAssetId(), command.getDto(), UUID.randomUUID().toString());
    }
}
