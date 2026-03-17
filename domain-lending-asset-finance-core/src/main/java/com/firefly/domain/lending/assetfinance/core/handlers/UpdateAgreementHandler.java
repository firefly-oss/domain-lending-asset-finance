package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAgreementApi;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import com.firefly.domain.lending.assetfinance.core.commands.UpdateAgreementCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link UpdateAgreementCommand} by delegating to core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class UpdateAgreementHandler extends CommandHandler<UpdateAgreementCommand, AssetFinanceAgreementDTO> {

    private final AssetFinanceAgreementApi agreementApi;

    /**
     * Creates a new UpdateAgreementHandler.
     *
     * @param agreementApi the SDK client for asset finance agreement operations
     */
    public UpdateAgreementHandler(AssetFinanceAgreementApi agreementApi) {
        this.agreementApi = agreementApi;
    }

    @Override
    protected Mono<AssetFinanceAgreementDTO> doHandle(UpdateAgreementCommand command) {
        log.debug("Updating asset finance agreement: agreementId={}", command.getAgreementId());
        return agreementApi.update(command.getAgreementId(), command.getDto(), UUID.randomUUID().toString());
    }
}
