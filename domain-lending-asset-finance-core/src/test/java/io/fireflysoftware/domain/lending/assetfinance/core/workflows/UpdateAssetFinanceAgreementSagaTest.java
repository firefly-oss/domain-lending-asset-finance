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
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateAgreementCommand;
import io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifies a field update keeps the current status and passes the product kind.
 */
@ExtendWith(MockitoExtension.class)
class UpdateAssetFinanceAgreementSagaTest {

    @Mock
    private CommandBus commandBus;

    @Mock
    private QueryBus queryBus;

    private UpdateAssetFinanceAgreementSaga saga;

    @BeforeEach
    void setUp() {
        saga = new UpdateAssetFinanceAgreementSaga(commandBus, queryBus);
    }

    @Test
    void update_passesRentingAndKeepsStatus() {
        UUID id = UUID.randomUUID();
        AssetFinanceAgreementDTO current = new AssetFinanceAgreementDTO(id);
        current.setFinanceType(AssetFinanceAgreementDTO.FinanceTypeEnum.LEASING);
        current.setAgreementStatus(AssetFinanceAgreementDTO.AgreementStatusEnum.ACTIVE);
        AssetFinanceAgreementDTO incoming = new AssetFinanceAgreementDTO();
        incoming.setFinanceType(AssetFinanceAgreementDTO.FinanceTypeEnum.RENTING);
        incoming.setAgreementStatus(AssetFinanceAgreementDTO.AgreementStatusEnum.CLOSED);
        when(commandBus.send(any(UpdateAgreementCommand.class))).thenAnswer(invocation ->
                Mono.just(invocation.getArgument(0, UpdateAgreementCommand.class).getDto()));

        ExecutionContext ctx = ExecutionContext.forSaga(AssetFinanceAgreementSagaConstants.SAGA_UPDATE_RECORD, "test");
        ctx.putVariable(AssetFinanceAgreementSagaConstants.CTX_RECORD, current);

        StepVerifier.create(saga.updateAssetFinanceAgreement(new UpdateAgreementCommand(id, incoming), ctx))
                .assertNext(saved -> {
                    assertThat(saved.getFinanceType()).isEqualTo(AssetFinanceAgreementDTO.FinanceTypeEnum.RENTING);
                    assertThat(saved.getAgreementStatus()).isEqualTo(AssetFinanceAgreementDTO.AgreementStatusEnum.ACTIVE);
                })
                .verifyComplete();

        ArgumentCaptor<UpdateAgreementCommand> captor = ArgumentCaptor.forClass(UpdateAgreementCommand.class);
        verify(commandBus).send(captor.capture());
        assertThat(captor.getValue().getDto().getFinanceType()).isEqualTo(AssetFinanceAgreementDTO.FinanceTypeEnum.RENTING);
    }
}
