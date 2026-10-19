package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAssetApi;
import com.firefly.core.lending.assetfinance.sdk.model.FilterRequestAssetFinanceAssetDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponseAssetFinanceAssetDTO;
import com.firefly.domain.lending.assetfinance.core.commands.GetAgreementAssetsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAgreementAssetsQuery} by listing assets from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAgreementAssetsHandler extends QueryHandler<GetAgreementAssetsQuery, PaginationResponseAssetFinanceAssetDTO> {

    private final AssetFinanceAssetApi assetApi;

    /**
     * Creates a new GetAgreementAssetsHandler.
     *
     * @param assetApi the SDK client for asset finance asset operations
     */
    public GetAgreementAssetsHandler(AssetFinanceAssetApi assetApi) {
        this.assetApi = assetApi;
    }

    @Override
    protected Mono<PaginationResponseAssetFinanceAssetDTO> doHandle(GetAgreementAssetsQuery query) {
        log.debug("Listing assets for agreement: agreementId={}", query.getAgreementId());
        return assetApi.findAll2(query.getAgreementId(), new FilterRequestAssetFinanceAssetDTO(), UUID.randomUUID().toString());
    }
}
