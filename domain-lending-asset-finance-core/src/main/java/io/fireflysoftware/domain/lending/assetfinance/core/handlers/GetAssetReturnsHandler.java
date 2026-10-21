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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.ReturnRecordApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.FilterRequestReturnRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseReturnRecordDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetReturnsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetReturnsQuery} by listing return records from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetReturnsHandler extends QueryHandler<GetAssetReturnsQuery, PaginationResponseReturnRecordDTO> {

    private final ReturnRecordApi returnApi;

    /**
     * Creates a new GetAssetReturnsHandler.
     *
     * @param returnApi the SDK client for return record operations
     */
    public GetAssetReturnsHandler(ReturnRecordApi returnApi) {
        this.returnApi = returnApi;
    }

    @Override
    protected Mono<PaginationResponseReturnRecordDTO> doHandle(GetAssetReturnsQuery query) {
        log.debug("Listing returns for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return returnApi.findAll5(query.getAgreementId(), query.getAssetId(), new FilterRequestReturnRecordDTO(), UUID.randomUUID().toString());
    }
}
