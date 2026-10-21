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

import io.fireflysoftware.core.lending.assetfinance.sdk.api.EndOptionApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.EndOptionDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.CreateEndOptionCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link CreateEndOptionCommand} by creating a lease-end option in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class CreateEndOptionHandler extends CommandHandler<CreateEndOptionCommand, EndOptionDTO> {

    private final EndOptionApi endOptionApi;

    /**
     * Creates a new CreateEndOptionHandler.
     *
     * @param endOptionApi the SDK client for end option operations
     */
    public CreateEndOptionHandler(EndOptionApi endOptionApi) {
        this.endOptionApi = endOptionApi;
    }

    @Override
    protected Mono<EndOptionDTO> doHandle(CreateEndOptionCommand command) {
        log.debug("Creating end option for agreement: agreementId={}", command.getAgreementId());
        return endOptionApi.create1(command.getAgreementId(), command.getDto(), UUID.randomUUID().toString());
    }
}
