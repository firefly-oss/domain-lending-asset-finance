package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAssetApi;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterAssetCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link RegisterAssetCommand} by registering a physical asset in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class RegisterAssetHandler extends CommandHandler<RegisterAssetCommand, AssetFinanceAssetDTO> {

    private final AssetFinanceAssetApi assetApi;

    /**
     * Creates a new RegisterAssetHandler.
     *
     * @param assetApi the SDK client for asset finance asset operations
     */
    public RegisterAssetHandler(AssetFinanceAssetApi assetApi) {
        this.assetApi = assetApi;
    }

    @Override
    protected Mono<AssetFinanceAssetDTO> doHandle(RegisterAssetCommand command) {
        log.debug("Registering asset for agreement: agreementId={}", command.getAgreementId());
        return assetApi.create2(command.getAgreementId(), command.getDto(), UUID.randomUUID().toString());
    }
}
