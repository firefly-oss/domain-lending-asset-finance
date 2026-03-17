package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.DeliveryRecordApi;
import com.firefly.core.lending.assetfinance.sdk.model.FilterRequestDeliveryRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetDeliveriesQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetDeliveriesQuery} by listing delivery records from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetDeliveriesHandler extends QueryHandler<GetAssetDeliveriesQuery, PaginationResponse> {

    private final DeliveryRecordApi deliveryApi;

    /**
     * Creates a new GetAssetDeliveriesHandler.
     *
     * @param deliveryApi the SDK client for delivery record operations
     */
    public GetAssetDeliveriesHandler(DeliveryRecordApi deliveryApi) {
        this.deliveryApi = deliveryApi;
    }

    @Override
    protected Mono<PaginationResponse> doHandle(GetAssetDeliveriesQuery query) {
        log.debug("Listing deliveries for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return deliveryApi.findAll7(query.getAgreementId(), query.getAssetId(), new FilterRequestDeliveryRecordDTO(), UUID.randomUUID().toString());
    }
}
