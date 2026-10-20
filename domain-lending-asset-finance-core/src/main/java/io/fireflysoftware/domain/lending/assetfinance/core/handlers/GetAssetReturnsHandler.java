package io.fireflysoftware.domain.lending.assetfinance.core.handlers;

import io.fireflysoftware.core.lending.assetfinance.sdk.api.ReturnRecordApi;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.FilterRequestReturnRecordDTO;
import io.fireflysoftware.core.lending.assetfinance.sdk.model.PaginationResponseReturnRecordDTO;
import io.fireflysoftware.domain.lending.assetfinance.core.commands.GetAssetReturnsQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handles {@link GetAssetReturnsQuery} by listing return records from core-lending-asset-finance.
 */
@Slf4j
@QueryHandlerComponent
public class GetAssetReturnsHandler extends QueryHandler<GetAssetReturnsQuery, PaginationResponseReturnRecordDTO> {

    private final ReturnRecordApi returnApi;

    /**
     * Creates a new GetAssetReturnsHandler.
     *
     * @param returnApi the SDK client for return record operations
     */
    public GetAssetReturnsHandler(ReturnRecordApi returnApi) {
        this.returnApi = returnApi;
    }

    @Override
    protected Mono<PaginationResponseReturnRecordDTO> doHandle(GetAssetReturnsQuery query) {
        log.debug("Listing returns for asset: agreementId={}, assetId={}", query.getAgreementId(), query.getAssetId());
        return returnApi.findAll5(query.getAgreementId(), query.getAssetId(), new FilterRequestReturnRecordDTO(), UUID.randomUUID().toString());
    }
}
