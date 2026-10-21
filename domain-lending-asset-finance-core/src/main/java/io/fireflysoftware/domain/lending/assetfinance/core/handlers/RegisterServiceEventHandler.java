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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.ServiceEventApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.ServiceEventDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.RegisterServiceEventCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link RegisterServiceEventCommand} by logging a service event in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class RegisterServiceEventHandler extends CommandHandler<RegisterServiceEventCommand, ServiceEventDTO> {

    private final ServiceEventApi serviceEventApi;

    /**
     * Creates a new RegisterServiceEventHandler.
     *
     * @param serviceEventApi the SDK client for service event operations
     */
    public RegisterServiceEventHandler(ServiceEventApi serviceEventApi) {
        this.serviceEventApi = serviceEventApi;
    }

    @Override
    protected Mono<ServiceEventDTO> doHandle(RegisterServiceEventCommand command) {
        log.debug("Registering service event for asset: agreementId={}, assetId={}", command.getAgreementId(), command.getAssetId());
        return serviceEventApi.create4(command.getAgreementId(), command.getAssetId(), command.getDto(), UUID.randomUUID().toString());
    }
}
