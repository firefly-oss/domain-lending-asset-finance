package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAssetApi;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetQuery} by fetching a single asset from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetHandler extends QueryHandler<GetAssetQuery, AssetFinanceAssetDTO> {

    private final AssetFinanceAssetApi assetApi;

    /**
     * Creates a new GetAssetHandler.
     *
     * @param assetApi the SDK client for asset finance asset operations
     */
    public GetAssetHandler(AssetFinanceAssetApi assetApi) {
        this.assetApi = assetApi;
    }

    @Override
    protected Mono<AssetFinanceAssetDTO> doHandle(GetAssetQuery query) {
        log.debug("Fetching asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return assetApi.getById2(query.getAgreementId(), query.getAssetId(), UUID.randomUUID().toString());
    }
}
