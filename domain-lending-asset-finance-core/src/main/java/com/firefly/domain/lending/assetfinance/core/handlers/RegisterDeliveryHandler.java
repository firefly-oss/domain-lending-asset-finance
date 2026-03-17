package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.DeliveryRecordApi;
import com.firefly.core.lending.assetfinance.sdk.model.DeliveryRecordDTO;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterDeliveryCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link RegisterDeliveryCommand} by creating a delivery record in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class RegisterDeliveryHandler extends CommandHandler<RegisterDeliveryCommand, DeliveryRecordDTO> {

    private final DeliveryRecordApi deliveryApi;

    /**
     * Creates a new RegisterDeliveryHandler.
     *
     * @param deliveryApi the SDK client for delivery record operations
     */
    public RegisterDeliveryHandler(DeliveryRecordApi deliveryApi) {
        this.deliveryApi = deliveryApi;
    }

    @Override
    protected Mono<DeliveryRecordDTO> doHandle(RegisterDeliveryCommand command) {
        log.debug("Registering delivery for asset: agreementId={}, assetId={}", command.getAgreementId(), command.getAssetId());
        return deliveryApi.create7(command.getAgreementId(), command.getAssetId(), command.getDto(), UUID.randomUUID().toString());
    }
}
