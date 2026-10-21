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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.ServiceEventApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.FilterRequestServiceEventDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseServiceEventDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetServiceEventsQuery;
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
public class GetAssetServiceEventsHandler extends QueryHandler<GetAssetServiceEventsQuery, PaginationResponseServiceEventDTO> {

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
    protected Mono<PaginationResponseServiceEventDTO> doHandle(GetAssetServiceEventsQuery query) {
        log.debug("Listing service events for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return serviceEventApi.findAll4(query.getAgreementId(), query.getAssetId(), new FilterRequestServiceEventDTO(), UUID.randomUUID().toString());
    }
}
