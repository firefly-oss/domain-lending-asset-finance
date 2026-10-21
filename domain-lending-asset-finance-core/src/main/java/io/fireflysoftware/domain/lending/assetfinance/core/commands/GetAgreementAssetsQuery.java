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


package io.fireflysoftware.domain.lending.assetfinance.core.commands;

import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseAssetFinanceAssetDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to list all physical assets registered under an agreement.
 */
public class GetAgreementAssetsQuery implements Query<PaginationResponseAssetFinanceAssetDTO> {

    @NotNull
    private final UUID agreementId;

    /**
     * Creates a new GetAgreementAssetsQuery.
     *
     * @param agreementId the unique identifier of the agreement
     */
    public GetAgreementAssetsQuery(UUID agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * Returns the agreement identifier.
     *
     * @return the agreement ID
     */
    public UUID getAgreementId() {
        return agreementId;
    }
}
