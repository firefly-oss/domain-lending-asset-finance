package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.EndOptionApi;
import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import com.firefly.domain.lending.assetfinance.core.commands.UpdateEndOptionCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link UpdateEndOptionCommand} by updating a lease-end option in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class UpdateEndOptionHandler extends CommandHandler<UpdateEndOptionCommand, EndOptionDTO> {

    private final EndOptionApi endOptionApi;

    /**
     * Creates a new UpdateEndOptionHandler.
     *
     * @param endOptionApi the SDK client for end option operations
     */
    public UpdateEndOptionHandler(EndOptionApi endOptionApi) {
        this.endOptionApi = endOptionApi;
    }

    @Override
    protected Mono<EndOptionDTO> doHandle(UpdateEndOptionCommand command) {
        log.debug("Updating end option: agreementId={}, optionId={}", command.getAgreementId(), command.getOptionId());
        return endOptionApi.update1(command.getAgreementId(), command.getOptionId(), command.getDto(), UUID.randomUUID().toString());
    }
}
