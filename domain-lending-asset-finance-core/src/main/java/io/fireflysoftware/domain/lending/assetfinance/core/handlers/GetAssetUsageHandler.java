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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.UsageRecordApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.FilterRequestUsageRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseUsageRecordDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetUsageQuery;
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
public class GetAssetUsageHandler extends QueryHandler<GetAssetUsageQuery, PaginationResponseUsageRecordDTO> {

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
    protected Mono<PaginationResponseUsageRecordDTO> doHandle(GetAssetUsageQuery query) {
        log.debug("Listing usage records for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return usageApi.findAll3(query.getAgreementId(), query.getAssetId(), new FilterRequestUsageRecordDTO(), UUID.randomUUID().toString());
    }
}
