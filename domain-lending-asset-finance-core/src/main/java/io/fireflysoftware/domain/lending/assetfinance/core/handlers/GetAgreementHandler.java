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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.AssetFinanceAgreementApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAgreementQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAgreementQuery} by fetching the agreement from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAgreementHandler extends QueryHandler<GetAgreementQuery, AssetFinanceAgreementDTO> {

    private final AssetFinanceAgreementApi agreementApi;

    /**
     * Creates a new GetAgreementHandler.
     *
     * @param agreementApi the SDK client for asset finance agreement operations
     */
    public GetAgreementHandler(AssetFinanceAgreementApi agreementApi) {
        this.agreementApi = agreementApi;
    }

    @Override
    protected Mono<AssetFinanceAgreementDTO> doHandle(GetAgreementQuery query) {
        log.debug("Fetching asset finance agreement: agreementId={}", query.getAgreementId());
        return agreementApi.getById(query.getAgreementId(), UUID.randomUUID().toString());
    }
}
