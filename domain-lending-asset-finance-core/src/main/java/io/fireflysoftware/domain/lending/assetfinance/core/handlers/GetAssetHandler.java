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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.AssetFinanceAssetApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetQuery} by fetching a single asset from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetHandler extends QueryHandler<GetAssetQuery, AssetFinanceAssetDTO> {

    private final AssetFinanceAssetApi assetApi;

    /**
     * Creates a new GetAssetHandler.
     *
     * @param assetApi the SDK client for asset finance asset operations
     */
    public GetAssetHandler(AssetFinanceAssetApi assetApi) {
        this.assetApi = assetApi;
    }

    @Override
    protected Mono<AssetFinanceAssetDTO> doHandle(GetAssetQuery query) {
        log.debug("Fetching asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return assetApi.getById2(query.getAgreementId(), query.getAssetId(), UUID.randomUUID().toString());
    }
}
