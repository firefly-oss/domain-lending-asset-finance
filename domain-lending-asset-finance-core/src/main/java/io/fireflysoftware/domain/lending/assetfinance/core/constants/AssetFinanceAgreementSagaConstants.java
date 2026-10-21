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

package io.fireflysoftware.domain.lending.assetfinance.core.constants;

/**
 * Names shared by the asset finance agreement sagas, their steps, and the saga engine.
 */
public final class AssetFinanceAgreementSagaConstants {

    public static final String SAGA_REGISTER = "RegisterAssetFinanceAgreementSaga";
    public static final String SAGA_UPDATE_STATUS = "UpdateAssetFinanceAgreementStatusSaga";
    public static final String SAGA_UPDATE_RECORD = "UpdateAssetFinanceAgreementSaga";

    public static final String STEP_REGISTER = "registerAssetFinanceAgreement";
    public static final String STEP_RETRIEVE = "retrieveAssetFinanceAgreement";
    public static final String STEP_UPDATE_STATUS = "updateAssetFinanceAgreementStatus";
    public static final String STEP_RETRIEVE_FOR_UPDATE = "retrieveAssetFinanceAgreementForUpdate";
    public static final String STEP_UPDATE_RECORD = "updateAssetFinanceAgreement";

    public static final String COMPENSATE_TERMINATE = "terminateCreatedAssetFinanceAgreement";
    public static final String COMPENSATE_RESTORE = "restoreAssetFinanceAgreementStatus";
    public static final String COMPENSATE_RESTORE_RECORD = "restoreAssetFinanceAgreement";

    public static final String EVENT_REGISTERED = "asset-finance-agreements.registered";
    public static final String EVENT_RETRIEVED = "asset-finance-agreements.retrieved";
    public static final String EVENT_STATUS_UPDATED = "asset-finance-agreements.status.updated";
    public static final String EVENT_RECORD_UPDATED = "asset-finance-agreements.updated";

    public static final String CTX_RECORD = "assetFinanceAgreementRecord";
    public static final String CTX_PREVIOUS_STATUS = "previousAssetFinanceAgreementStatus";
    public static final String CTX_PREVIOUS_REMARKS = "previousAssetFinanceAgreementRemarks";
    public static final String CTX_PREVIOUS_RECORD = "previousAssetFinanceAgreementRecord";

    private AssetFinanceAgreementSagaConstants() {
    }
}
