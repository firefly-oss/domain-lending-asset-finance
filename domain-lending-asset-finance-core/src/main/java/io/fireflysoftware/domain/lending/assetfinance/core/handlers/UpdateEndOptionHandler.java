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
import io.fireflysoftware.domain.lending.assetfinance.core.commands.UpdateEndOptionCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handles {@link UpdateEndOptionCommand} by updating a lease-end option in core-lending-asset-finance.
 */
@Slf4j
@CommandHandlerComponent
public class UpdateEndOptionHandler extends CommandHandler<UpdateEndOptionCommand, EndOptionDTO> {

    private final EndOptionApi endOptionApi;

    /**
     * Creates a new UpdateEndOptionHandler.
     *
     * @param endOptionApi the SDK client for end option operations
     */
    public UpdateEndOptionHandler(EndOptionApi endOptionApi) {
        this.endOptionApi = endOptionApi;
    }

    @Override
    protected Mono<EndOptionDTO> doHandle(UpdateEndOptionCommand command) {
        log.debug("Updating end option: agreementId={}, optionId={}", command.getAgreementId(), command.getOptionId());
        return endOptionApi.update1(command.getAgreementId(), command.getOptionId(), command.getDto(), UUID.randomUUID().toString());
    }
}
