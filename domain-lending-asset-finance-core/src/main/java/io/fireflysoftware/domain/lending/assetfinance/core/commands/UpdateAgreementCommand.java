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

import io.fireflysoftware.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to update an existing asset finance agreement.
 */
public class UpdateAgreementCommand implements Command<AssetFinanceAgreementDTO> {

    @NotNull
    private final UUID agreementId;

    @NotNull
    private final AssetFinanceAgreementDTO dto;

    /**
     * Creates a new UpdateAgreementCommand.
     *
     * @param agreementId the unique identifier of the agreement to update
     * @param dto         the updated agreement payload
     */
    public UpdateAgreementCommand(UUID agreementId, AssetFinanceAgreementDTO dto) {
        this.agreementId = agreementId;
        this.dto = dto;
    }

    /**
     * Returns the agreement identifier.
     *
     * @return the agreement ID
     */
    public UUID getAgreementId() {
        return agreementId;
    }

    /**
     * Returns the updated agreement payload.
     *
     * @return the agreement DTO
     */
    public AssetFinanceAgreementDTO getDto() {
        return dto;
    }
}
