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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.PickupRecordApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PickupRecordDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.RegisterPickupCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link RegisterPickupCommand} by creating a pickup record in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class RegisterPickupHandler extends CommandHandler<RegisterPickupCommand, PickupRecordDTO> {

    private final PickupRecordApi pickupApi;

    /**
     * Creates a new RegisterPickupHandler.
     *
     * @param pickupApi the SDK client for pickup record operations
     */
    public RegisterPickupHandler(PickupRecordApi pickupApi) {
        this.pickupApi = pickupApi;
    }

    @Override
    protected Mono<PickupRecordDTO> doHandle(RegisterPickupCommand command) {
        log.debug("Registering pickup for asset: agreementId={}, assetId={}", command.getAgreementId(), command.getAssetId());
        return pickupApi.create6(command.getAgreementId(), command.getAssetId(), command.getDto(), UUID.randomUUID().toString());
    }
}
