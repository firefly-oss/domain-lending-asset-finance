package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.PickupRecordApi;
import com.firefly.core.lending.assetfinance.sdk.model.PickupRecordDTO;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterPickupCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link RegisterPickupCommand} by creating a pickup record in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class RegisterPickupHandler extends CommandHandler<RegisterPickupCommand, PickupRecordDTO> {

    private final PickupRecordApi pickupApi;

    /**
     * Creates a new RegisterPickupHandler.
     *
     * @param pickupApi the SDK client for pickup record operations
     */
    public RegisterPickupHandler(PickupRecordApi pickupApi) {
        this.pickupApi = pickupApi;
    }

    @Override
    protected Mono<PickupRecordDTO> doHandle(RegisterPickupCommand command) {
        log.debug("Registering pickup for asset: agreementId={}, assetId={}", command.getAgreementId(), command.getAssetId());
        return pickupApi.create6(command.getAgreementId(), command.getAssetId(), command.getDto(), UUID.randomUUID().toString());
    }
}
