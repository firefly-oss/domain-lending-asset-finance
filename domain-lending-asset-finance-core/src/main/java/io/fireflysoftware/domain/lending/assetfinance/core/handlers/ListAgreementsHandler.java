/*
 * Copyright 2026 Firefly Software Foundation
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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.AssetFinanceAgreementApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.FilterRequestAssetFinanceAgreementDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseAssetFinanceAgreementDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.ListAgreementsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Lists agreements through the asset finance core's existing find-all operation.
 */
@Slf4j
@QueryHandlerComponent
public class ListAgreementsHandler extends QueryHandler<ListAgreementsQuery, PaginationResponseAssetFinanceAgreementDTO> {

    private final AssetFinanceAgreementApi agreementApi;

    public ListAgreementsHandler(AssetFinanceAgreementApi agreementApi) {
        this.agreementApi = agreementApi;
    }

    @Override
    protected Mono<PaginationResponseAssetFinanceAgreementDTO> doHandle(ListAgreementsQuery query) {
        log.debug("Listing asset finance agreements");
        return agreementApi.findAll(new FilterRequestAssetFinanceAgreementDTO(), UUID.randomUUID().toString());
    }
}
