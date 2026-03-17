package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.ServiceEventApi;
import com.firefly.core.lending.assetfinance.sdk.model.FilterRequestServiceEventDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetServiceEventsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetServiceEventsQuery} by listing service events from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetServiceEventsHandler extends QueryHandler<GetAssetServiceEventsQuery, PaginationResponse> {

    private final ServiceEventApi serviceEventApi;

    /**
     * Creates a new GetAssetServiceEventsHandler.
     *
     * @param serviceEventApi the SDK client for service event operations
     */
    public GetAssetServiceEventsHandler(ServiceEventApi serviceEventApi) {
        this.serviceEventApi = serviceEventApi;
    }

    @Override
    protected Mono<PaginationResponse> doHandle(GetAssetServiceEventsQuery query) {
        log.debug("Listing service events for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return serviceEventApi.findAll4(query.getAgreementId(), query.getAssetId(), new FilterRequestServiceEventDTO(), UUID.randomUUID().toString());
    }
}
