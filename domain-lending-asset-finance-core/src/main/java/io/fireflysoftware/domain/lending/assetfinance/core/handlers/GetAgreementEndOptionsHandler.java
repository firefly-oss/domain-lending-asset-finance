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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.EndOptionApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.FilterRequestEndOptionDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseEndOptionDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAgreementEndOptionsQuery;
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
public class GetAgreementEndOptionsHandler extends QueryHandler<GetAgreementEndOptionsQuery, PaginationResponseEndOptionDTO> {

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
    protected Mono<PaginationResponseEndOptionDTO> doHandle(GetAgreementEndOptionsQuery query) {
        log.debug("Listing end options for agreement: agreementId={}", query.getAgreementId());
        return endOptionApi.findAll1(query.getAgreementId(), new FilterRequestEndOptionDTO(), UUID.randomUUID().toString());
    }
}
