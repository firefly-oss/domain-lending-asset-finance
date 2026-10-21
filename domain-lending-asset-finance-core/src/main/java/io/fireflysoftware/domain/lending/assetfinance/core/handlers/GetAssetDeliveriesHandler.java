/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.domain.lending.assetfinance.core.handlers;

import io.fireflysoftware.core.lending.assetfinance.sdk.api.DeliveryRecordApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.FilterRequestDeliveryRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseDeliveryRecordDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetDeliveriesQuery;
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
public class GetAssetDeliveriesHandler extends QueryHandler<GetAssetDeliveriesQuery, PaginationResponseDeliveryRecordDTO> {

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
    protected Mono<PaginationResponseDeliveryRecordDTO> doHandle(GetAssetDeliveriesQuery query) {
        log.debug("Listing deliveries for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return deliveryApi.findAll7(query.getAgreementId(), query.getAssetId(), new FilterRequestDeliveryRecordDTO(), UUID.randomUUID().toString());
    }
}
