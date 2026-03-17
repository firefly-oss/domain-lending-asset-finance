package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.EndOptionApi;
import com.firefly.core.lending.assetfinance.sdk.model.FilterRequestEndOptionDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.domain.lending.assetfinance.core.commands.GetAgreementEndOptionsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAgreementEndOptionsQuery} by listing end options from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAgreementEndOptionsHandler extends QueryHandler<GetAgreementEndOptionsQuery, PaginationResponse> {

    private final EndOptionApi endOptionApi;

    /**
     * Creates a new GetAgreementEndOptionsHandler.
     *
     * @param endOptionApi the SDK client for end option operations
     */
    public GetAgreementEndOptionsHandler(EndOptionApi endOptionApi) {
        this.endOptionApi = endOptionApi;
    }

    @Override
    protected Mono<PaginationResponse> doHandle(GetAgreementEndOptionsQuery query) {
        log.debug("Listing end options for agreement: agreementId={}", query.getAgreementId());
        return endOptionApi.findAll1(query.getAgreementId(), new FilterRequestEndOptionDTO(), UUID.randomUUID().toString());
    }
}
