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
import io.fireflysoftware.domain.lending.assetfinance.core.commands.CreateAgreementCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateAgreementCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.lifecycle.AssetFinanceAgreementLifecycle;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.fireflyframework.orchestration.saga.annotation.Saga;
import org.fireflyframework.orchestration.saga.annotation.SagaStep;
import org.fireflyframework.orchestration.saga.annotation.StepEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.COMPENSATE_TERMINATE;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.CTX_RECORD;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.EVENT_REGISTERED;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.SAGA_REGISTER;
import static io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants.STEP_REGISTER;

/**
 * Creates an asset finance agreement and compensates by terminating it.
 * The product kind on the command is sent through unchanged.
 */
@Saga(name = SAGA_REGISTER)
@Service
public class RegisterAssetFinanceAgreementSaga {

    private final CommandBus commandBus;

    @Autowired
    public RegisterAssetFinanceAgreementSaga(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @SagaStep(id = STEP_REGISTER, compensate = COMPENSATE_TERMINATE)
    @StepEvent(type = EVENT_REGISTERED)
    public Mono<AssetFinanceAgreementDTO> registerAssetFinanceAgreement(CreateAgreementCommand command, ExecutionContext ctx) {
        if (command.getDto() == null || command.getDto().getFinanceType() == null) {
            return Mono.error(new IllegalArgumentException("Finance type is required"));
        }
        return commandBus.send(command)
                .doOnNext(created -> ctx.putVariable(CTX_RECORD, created));
    }

    public Mono<Void> terminateCreatedAssetFinanceAgreement(AssetFinanceAgreementDTO created) {
        if (created == null || created.getAssetFinanceAgreementId() == null || created.getAgreementStatus() == null) {
            return Mono.empty();
        }
        if (!AssetFinanceAgreementLifecycle.canTransition(
                created.getAgreementStatus(), AssetFinanceAgreementDTO.AgreementStatusEnum.TERMINATED)) {
            return Mono.empty();
        }
        created.setAgreementStatus(AssetFinanceAgreementDTO.AgreementStatusEnum.TERMINATED);
        return commandBus.send(new UpdateAgreementCommand(created.getAssetFinanceAgreementId(), created)).then();
    }
}
