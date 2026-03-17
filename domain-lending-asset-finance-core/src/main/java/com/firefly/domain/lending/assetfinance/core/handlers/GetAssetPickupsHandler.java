package com.firefly.domain.lending.assetfinance.core.handlers;

import com.firefly.core.lending.assetfinance.sdk.api.PickupRecordApi;
import com.firefly.core.lending.assetfinance.sdk.model.FilterRequestPickupRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetPickupsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetPickupsQuery} by listing pickup records from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetPickupsHandler extends QueryHandler<GetAssetPickupsQuery, PaginationResponse> {

    private final PickupRecordApi pickupApi;

    /**
     * Creates a new GetAssetPickupsHandler.
     *
     * @param pickupApi the SDK client for pickup record operations
     */
    public GetAssetPickupsHandler(PickupRecordApi pickupApi) {
        this.pickupApi = pickupApi;
    }

    @Override
    protected Mono<PaginationResponse> doHandle(GetAssetPickupsQuery query) {
        log.debug("Listing pickups for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return pickupApi.findAll6(query.getAgreementId(), query.getAssetId(), new FilterRequestPickupRecordDTO(), UUID.randomUUID().toString());
    }
}
