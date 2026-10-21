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
import io.fireflysoftware.domain.lending.assetfinance.core.constants.AssetFinanceAgreementSagaConstants;
import org.fireflyframework.cqrs.command.CommandBus;
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
 * Verifies create forwards the product kind and compensates by terminating the agreement.
 */
@ExtendWith(MockitoExtension.class)
class RegisterAssetFinanceAgreementSagaTest {

    @Mock
    private CommandBus commandBus;

    private RegisterAssetFinanceAgreementSaga saga;

    @BeforeEach
    void setUp() {
        saga = new RegisterAssetFinanceAgreementSaga(commandBus);
    }

    @Test
    void register_passesLeasingThrough() {
        UUID id = UUID.randomUUID();
        AssetFinanceAgreementDTO request = new AssetFinanceAgreementDTO();
        request.setFinanceType(AssetFinanceAgreementDTO.FinanceTypeEnum.LEASING);
        AssetFinanceAgreementDTO created = new AssetFinanceAgreementDTO(id);
        created.setFinanceType(AssetFinanceAgreementDTO.FinanceTypeEnum.LEASING);
        when(commandBus.send(any(CreateAgreementCommand.class))).thenReturn(Mono.just(created));

        ExecutionContext ctx = ExecutionContext.forSaga(AssetFinanceAgreementSagaConstants.SAGA_REGISTER, "test");

        StepVerifier.create(saga.registerAssetFinanceAgreement(new CreateAgreementCommand(request), ctx))
                .expectNext(created)
                .verifyComplete();

        ArgumentCaptor<CreateAgreementCommand> captor = ArgumentCaptor.forClass(CreateAgreementCommand.class);
        verify(commandBus).send(captor.capture());
        assertThat(captor.getValue().getDto().getFinanceType()).isEqualTo(AssetFinanceAgreementDTO.FinanceTypeEnum.LEASING);
    }

    @Test
    void register_rejectsAMissingProductKind() {
        ExecutionContext ctx = ExecutionContext.forSaga(AssetFinanceAgreementSagaConstants.SAGA_REGISTER, "test");

        StepVerifier.create(saga.registerAssetFinanceAgreement(new CreateAgreementCommand(new AssetFinanceAgreementDTO()), ctx))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void compensate_terminatesTheCreatedAgreement() {
        UUID id = UUID.randomUUID();
        AssetFinanceAgreementDTO created = new AssetFinanceAgreementDTO(id);
        created.setFinanceType(AssetFinanceAgreementDTO.FinanceTypeEnum.RENTING);
        created.setAgreementStatus(AssetFinanceAgreementDTO.AgreementStatusEnum.ACTIVE);
        when(commandBus.send(any(UpdateAgreementCommand.class))).thenReturn(Mono.just(created));

        StepVerifier.create(saga.terminateCreatedAssetFinanceAgreement(created))
                .verifyComplete();

        ArgumentCaptor<UpdateAgreementCommand> captor = ArgumentCaptor.forClass(UpdateAgreementCommand.class);
        verify(commandBus).send(captor.capture());
        assertThat(captor.getValue().getAgreementId()).isEqualTo(id);
        assertThat(captor.getValue().getDto().getAgreementStatus()).isEqualTo(AssetFinanceAgreementDTO.AgreementStatusEnum.TERMINATED);
        assertThat(captor.getValue().getDto().getFinanceType()).isEqualTo(AssetFinanceAgreementDTO.FinanceTypeEnum.RENTING);
    }
}
