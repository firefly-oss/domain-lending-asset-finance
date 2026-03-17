package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAgreementApi;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import com.firefly.domain.lending.assetfinance.core.commands.CreateAgreementCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link CreateAgreementCommand} by delegating to core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class CreateAgreementHandler extends CommandHandler<CreateAgreementCommand, AssetFinanceAgreementDTO> {

    private final AssetFinanceAgreementApi agreementApi;

    /**
     * Creates a new CreateAgreementHandler.
     *
     * @param agreementApi the SDK client for asset finance agreement operations
     */
    public CreateAgreementHandler(AssetFinanceAgreementApi agreementApi) {
        this.agreementApi = agreementApi;
    }

    @Override
    protected Mono<AssetFinanceAgreementDTO> doHandle(CreateAgreementCommand command) {
        log.debug("Creating asset finance agreement");
        return agreementApi.create(command.getDto(), UUID.randomUUID().toString());
    }
}
