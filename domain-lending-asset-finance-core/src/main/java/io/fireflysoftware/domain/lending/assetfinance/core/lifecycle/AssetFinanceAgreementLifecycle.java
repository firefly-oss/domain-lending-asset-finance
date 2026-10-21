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

package io.fireflysoftware.domain.lending.assetfinance.core.lifecycle;

import io.fireflysoftware.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Moves copied from the agreement lifecycle on {@code core-lending-asset-finance}.
 */
public final class AssetFinanceAgreementLifecycle {

    private static final Map<AssetFinanceAgreementDTO.AgreementStatusEnum, Set<AssetFinanceAgreementDTO.AgreementStatusEnum>> ALLOWED = Map.of(
            AssetFinanceAgreementDTO.AgreementStatusEnum.ACTIVE, EnumSet.of(
                    AssetFinanceAgreementDTO.AgreementStatusEnum.SUSPENDED,
                    AssetFinanceAgreementDTO.AgreementStatusEnum.CLOSED,
                    AssetFinanceAgreementDTO.AgreementStatusEnum.TERMINATED,
                    AssetFinanceAgreementDTO.AgreementStatusEnum.DEFAULTED),
            AssetFinanceAgreementDTO.AgreementStatusEnum.SUSPENDED, EnumSet.of(
                    AssetFinanceAgreementDTO.AgreementStatusEnum.ACTIVE,
                    AssetFinanceAgreementDTO.AgreementStatusEnum.CLOSED,
                    AssetFinanceAgreementDTO.AgreementStatusEnum.TERMINATED,
                    AssetFinanceAgreementDTO.AgreementStatusEnum.DEFAULTED),
            AssetFinanceAgreementDTO.AgreementStatusEnum.DEFAULTED, EnumSet.of(
                    AssetFinanceAgreementDTO.AgreementStatusEnum.CLOSED,
                    AssetFinanceAgreementDTO.AgreementStatusEnum.TERMINATED)
    );

    private AssetFinanceAgreementLifecycle() {
    }

    public static boolean canTransition(AssetFinanceAgreementDTO.AgreementStatusEnum from, AssetFinanceAgreementDTO.AgreementStatusEnum to) {
        if (from == to) {
            return true;
        }
        if (from == null || to == null) {
            return false;
        }
        Set<AssetFinanceAgreementDTO.AgreementStatusEnum> next = ALLOWED.get(from);
        return next != null && next.contains(to);
    }
}
