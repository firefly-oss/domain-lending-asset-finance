package com.firefly.domain.lending.assetfinance.core.services;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import com.firefly.core.lending.assetfinance.sdk.model.DeliveryRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponseAssetFinanceAssetDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponseDeliveryRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponsePickupRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponseReturnRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponseServiceEventDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponseUsageRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponseEndOptionDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PickupRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.ReturnRecordDTO;
import com.firefly.core.lending.assetfinance.sdk.model.ServiceEventDTO;
import com.firefly.core.lending.assetfinance.sdk.model.UsageRecordDTO;
import com.firefly.domain.lending.assetfinance.core.commands.CreateAgreementCommand;
import com.firefly.domain.lending.assetfinance.core.commands.CreateEndOptionCommand;
import com.firefly.domain.lending.assetfinance.core.commands.GetAgreementAssetsQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAgreementEndOptionsQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAgreementQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetDeliveriesQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetPickupsQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetReturnsQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetServiceEventsQuery;
import com.firefly.domain.lending.assetfinance.core.commands.GetAssetUsageQuery;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterAssetCommand;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterDeliveryCommand;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterPickupCommand;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterReturnCommand;
import com.firefly.domain.lending.assetfinance.core.commands.RegisterServiceEventCommand;
import com.firefly.domain.lending.assetfinance.core.commands.ReportUsageCommand;
import com.firefly.domain.lending.assetfinance.core.commands.UpdateAgreementCommand;
import com.firefly.domain.lending.assetfinance.core.commands.UpdateEndOptionCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Default implementation of {@link AssetFinanceService}.
 *
 * <p>Acts as a thin dispatcher: creates the appropriate Command or Query and sends it
 * via the CQRS bus, leaving all SDK interactions to the individual handlers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssetFinanceServiceImpl implements AssetFinanceService {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    // ── Agreements ──────────────────────────────────────────────────────────

    @Override
    public Mono<AssetFinanceAgreementDTO> createAgreement(AssetFinanceAgreementDTO dto) {
        return commandBus.send(new CreateAgreementCommand(dto));
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> getAgreement(UUID agreementId) {
        return queryBus.query(new GetAgreementQuery(agreementId));
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> updateAgreement(UUID agreementId, AssetFinanceAgreementDTO dto) {
        return commandBus.send(new UpdateAgreementCommand(agreementId, dto));
    }

    // ── Assets ───────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseAssetFinanceAssetDTO> getAgreementAssets(UUID agreementId) {
        return queryBus.query(new GetAgreementAssetsQuery(agreementId));
    }

    @Override
    public Mono<AssetFinanceAssetDTO> registerAsset(UUID agreementId, AssetFinanceAssetDTO dto) {
        return commandBus.send(new RegisterAssetCommand(agreementId, dto));
    }

    @Override
    public Mono<AssetFinanceAssetDTO> getAsset(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetQuery(agreementId, assetId));
    }

    // ── Deliveries ───────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseDeliveryRecordDTO> getAssetDeliveries(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetDeliveriesQuery(agreementId, assetId));
    }

    @Override
    public Mono<DeliveryRecordDTO> registerDelivery(UUID agreementId, UUID assetId, DeliveryRecordDTO dto) {
        return commandBus.send(new RegisterDeliveryCommand(agreementId, assetId, dto));
    }

    // ── Pickups ──────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponsePickupRecordDTO> getAssetPickups(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetPickupsQuery(agreementId, assetId));
    }

    @Override
    public Mono<PickupRecordDTO> registerPickup(UUID agreementId, UUID assetId, PickupRecordDTO dto) {
        return commandBus.send(new RegisterPickupCommand(agreementId, assetId, dto));
    }

    // ── Returns ──────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseReturnRecordDTO> getAssetReturns(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetReturnsQuery(agreementId, assetId));
    }

    @Override
    public Mono<ReturnRecordDTO> registerReturn(UUID agreementId, UUID assetId, ReturnRecordDTO dto) {
        return commandBus.send(new RegisterReturnCommand(agreementId, assetId, dto));
    }

    // ── Service Events ───────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseServiceEventDTO> getAssetServiceEvents(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetServiceEventsQuery(agreementId, assetId));
    }

    @Override
    public Mono<ServiceEventDTO> registerServiceEvent(UUID agreementId, UUID assetId, ServiceEventDTO dto) {
        return commandBus.send(new RegisterServiceEventCommand(agreementId, assetId, dto));
    }

    // ── Usage ────────────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseUsageRecordDTO> getAssetUsage(UUID agreementId, UUID assetId) {
        return queryBus.query(new GetAssetUsageQuery(agreementId, assetId));
    }

    @Override
    public Mono<UsageRecordDTO> reportUsage(UUID agreementId, UUID assetId, UsageRecordDTO dto) {
        return commandBus.send(new ReportUsageCommand(agreementId, assetId, dto));
    }

    // ── End Options ──────────────────────────────────────────────────────────

    @Override
    public Mono<PaginationResponseEndOptionDTO> getAgreementEndOptions(UUID agreementId) {
        return queryBus.query(new GetAgreementEndOptionsQuery(agreementId));
    }

    @Override
    public Mono<EndOptionDTO> createEndOption(UUID agreementId, EndOptionDTO dto) {
        return commandBus.send(new CreateEndOptionCommand(agreementId, dto));
    }

    @Override
    public Mono<EndOptionDTO> updateEndOption(UUID agreementId, UUID optionId, EndOptionDTO dto) {
        return commandBus.send(new UpdateEndOptionCommand(agreementId, optionId, dto));
    }
}
