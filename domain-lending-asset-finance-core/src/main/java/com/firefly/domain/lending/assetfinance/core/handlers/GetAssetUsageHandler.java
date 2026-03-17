package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.UsageRecordApi;
import com.firefly.core.lending.assetfinance.sdk.model.FilterRequestUsageRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetUsageQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetUsageQuery} by listing usage records from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetUsageHandler extends QueryHandler<GetAssetUsageQuery, PaginationResponse> {

    private final UsageRecordApi usageApi;

    /**
     * Creates a new GetAssetUsageHandler.
     *
     * @param usageApi the SDK client for usage record operations
     */
    public GetAssetUsageHandler(UsageRecordApi usageApi) {
        this.usageApi = usageApi;
    }

    @Override
    protected Mono<PaginationResponse> doHandle(GetAssetUsageQuery query) {
        log.debug("Listing usage records for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return usageApi.findAll3(query.getAgreementId(), query.getAssetId(), new FilterRequestUsageRecordDTO(), UUID.randomUUID().toString());
    }
}
