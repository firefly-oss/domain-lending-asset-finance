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

package io.fireflysoftware.domain.lending.assetfinance.core.workflows;

import io.fireflysoftware.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAgreementQuery;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateAgreementCommand;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.fireflyframework.orchestration.saga.annotation.Saga;
import org.fireflyframework.orchestration.saga.annotation.SagaStep;
import org.fireflyframework.orchestration.saga.annotation.StepEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.COMPENSATE_RESTORE_RECORD;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.CTX_PREVIOUS_RECORD;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.CTX_RECORD;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.EVENT_RECORD_UPDATED;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.EVENT_RETRIEVED;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.SAGA_UPDATE_RECORD;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.STEP_RETRIEVE_FOR_UPDATE;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.STEP_UPDATE_RECORD;

/**
 * Updates an asset finance agreement and compensates by writing the previous record back.
 * Status stays on the status saga. The product kind on the request is passed through.
 */
@Saga(name = SAGA_UPDATE_RECORD)
@Service
public class UpdateAssetFinanceAgreementSaga {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Autowired
    public UpdateAssetFinanceAgreementSaga(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @SagaStep(id = STEP_RETRIEVE_FOR_UPDATE)
    @StepEvent(type = EVENT_RETRIEVED)
    public Mono<AssetFinanceAgreementDTO> retrieveAssetFinanceAgreementForUpdate(GetAgreementQuery query, ExecutionContext ctx) {
        return queryBus.query(query)
                .doOnNext(record -> ctx.putVariable(CTX_RECORD, record));
    }

    @SagaStep(id = STEP_UPDATE_RECORD, dependsOn = STEP_RETRIEVE_FOR_UPDATE, compensate = COMPENSATE_RESTORE_RECORD)
    @StepEvent(type = EVENT_RECORD_UPDATED)
    public Mono<AssetFinanceAgreementDTO> updateAssetFinanceAgreement(UpdateAgreementCommand command, ExecutionContext ctx) {
        AssetFinanceAgreementDTO current = ctx.getVariableAs(CTX_RECORD, AssetFinanceAgreementDTO.class);
        if (current == null || current.getAssetFinanceAgreementId() == null) {
            return Mono.error(new IllegalStateException("Asset finance agreement was not found"));
        }
        AssetFinanceAgreementDTO incoming = command.getDto();
        if (incoming == null) {
            return Mono.error(new IllegalArgumentException("Asset finance agreement is required"));
        }
        if (incoming.getFinanceType() == null) {
            incoming.setFinanceType(current.getFinanceType());
        }
        if (incoming.getFinanceType() == null) {
            return Mono.error(new IllegalArgumentException("Finance type is required"));
        }
        if (current.getAgreementStatus() != null) {
            incoming.setAgreementStatus(current.getAgreementStatus());
        }
        ctx.putVariable(CTX_PREVIOUS_RECORD, current);
        return commandBus.send(new UpdateAgreementCommand(current.getAssetFinanceAgreementId(), incoming))
                .doOnNext(updated -> ctx.putVariable(CTX_RECORD, updated));
    }

    public Mono<Void> restoreAssetFinanceAgreement(AssetFinanceAgreementDTO updated, ExecutionContext ctx) {
        AssetFinanceAgreementDTO previous = ctx.getVariableAs(CTX_PREVIOUS_RECORD, AssetFinanceAgreementDTO.class);
        if (previous == null || previous.getAssetFinanceAgreementId() == null) {
            return Mono.empty();
        }
        return commandBus.send(new UpdateAgreementCommand(previous.getAssetFinanceAgreementId(), previous)).then();
    }
}
