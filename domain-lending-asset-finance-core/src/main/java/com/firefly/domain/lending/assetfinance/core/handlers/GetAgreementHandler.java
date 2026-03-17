package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAgreementApi;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import com.firefly.domain.lending.assetfinance.core.commands.GetAgreementQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAgreementQuery} by fetching the agreement from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAgreementHandler extends QueryHandler<GetAgreementQuery, AssetFinanceAgreementDTO> {

    private final AssetFinanceAgreementApi agreementApi;

    /**
     * Creates a new GetAgreementHandler.
     *
     * @param agreementApi the SDK client for asset finance agreement operations
     */
    public GetAgreementHandler(AssetFinanceAgreementApi agreementApi) {
        this.agreementApi = agreementApi;
    }

    @Override
    protected Mono<AssetFinanceAgreementDTO> doHandle(GetAgreementQuery query) {
        log.debug("Fetching asset finance agreement: agreementId={}", query.getAgreementId());
        return agreementApi.getById(query.getAgreementId(), UUID.randomUUID().toString());
    }
}
