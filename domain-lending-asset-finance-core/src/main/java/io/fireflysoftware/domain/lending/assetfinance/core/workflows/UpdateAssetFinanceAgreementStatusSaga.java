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
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateAgreementStatusCommand;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.fireflyframework.orchestration.saga.annotation.Saga;
import org.fireflyframework.orchestration.saga.annotation.SagaStep;
import org.fireflyframework.orchestration.saga.annotation.StepEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.COMPENSATE_RESTORE;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.CTX_PREVIOUS_REMARKS;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.CTX_PREVIOUS_STATUS;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.CTX_RECORD;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.EVENT_RETRIEVED;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.EVENT_STATUS_UPDATED;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.SAGA_UPDATE_STATUS;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.STEP_RETRIEVE;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.STEP_UPDATE_STATUS;

/**
 * Changes an asset finance agreement status and leaves the product kind on the current record.
 */
@Saga(name = SAGA_UPDATE_STATUS)
@Service
public class UpdateAssetFinanceAgreementStatusSaga {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Autowired
    public UpdateAssetFinanceAgreementStatusSaga(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @SagaStep(id = STEP_RETRIEVE)
    @StepEvent(type = EVENT_RETRIEVED)
    public Mono<AssetFinanceAgreementDTO> retrieveAssetFinanceAgreement(GetAgreementQuery query, ExecutionContext ctx) {
        return queryBus.query(query)
                .doOnNext(record -> ctx.putVariable(CTX_RECORD, record));
    }

    @SagaStep(id = STEP_UPDATE_STATUS, dependsOn = STEP_RETRIEVE, compensate = COMPENSATE_RESTORE)
    @StepEvent(type = EVENT_STATUS_UPDATED)
    public Mono<AssetFinanceAgreementDTO> updateAssetFinanceAgreementStatus(UpdateAgreementStatusCommand command, ExecutionContext ctx) {
        AssetFinanceAgreementDTO current = ctx.getVariableAs(CTX_RECORD, AssetFinanceAgreementDTO.class);
        if (current == null || current.getAssetFinanceAgreementId() == null) {
            return Mono.error(new IllegalStateException("Asset finance agreement was not found"));
        }
        if (current.getFinanceType() == null) {
            return Mono.error(new IllegalStateException("Asset finance agreement has no product kind"));
        }
        if (current.getAgreementStatus() == null) {
            return Mono.error(new IllegalStateException("Asset finance agreement has no current status"));
        }
        if (command.status() == null || command.status().isBlank()) {
            return Mono.error(new IllegalArgumentException("Status is required"));
        }
        ctx.putVariable(CTX_PREVIOUS_STATUS, current.getAgreementStatus().getValue());
        ctx.putVariable(CTX_PREVIOUS_REMARKS, current.getRemarks() == null ? "" : current.getRemarks());
        current.setAgreementStatus(AssetFinanceAgreementDTO.AgreementStatusEnum.fromValue(command.status()));
        if (command.statusReason() != null && !command.statusReason().isBlank()) {
            String existing = current.getRemarks();
            String note = command.statusReason().trim();
            current.setRemarks(existing == null || existing.isBlank() ? note : existing + "\n" + note);
        }
        return commandBus.send(new UpdateAgreementCommand(current.getAssetFinanceAgreementId(), current))
                .doOnNext(updated -> ctx.putVariable(CTX_RECORD, updated));
    }

    public Mono<Void> restoreAssetFinanceAgreementStatus(AssetFinanceAgreementDTO updated, ExecutionContext ctx) {
        if (updated == null || updated.getAssetFinanceAgreementId() == null) {
            return Mono.empty();
        }
        String previous = ctx.getVariableAs(CTX_PREVIOUS_STATUS, String.class);
        if (previous == null || previous.isBlank()) {
            return Mono.empty();
        }
        updated.setAgreementStatus(AssetFinanceAgreementDTO.AgreementStatusEnum.fromValue(previous));
        String previousRemarks = ctx.getVariableAs(CTX_PREVIOUS_REMARKS, String.class);
        updated.setRemarks(previousRemarks == null || previousRemarks.isEmpty() ? null : previousRemarks);
        return commandBus.send(new UpdateAgreementCommand(updated.getAssetFinanceAgreementId(), updated)).then();
    }
}
