package com.firefly.domain.lending.assetfinance.web.controllers;

import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAgreementDTO;
import com.firefly.core.lending.assetfinance.sdk.model.AssetFinanceAssetDTO;
import com.firefly.core.lending.assetfinance.sdk.model.EndOptionDTO;
import com.firefly.core.lending.assetfinance.sdk.model.PaginationResponse;
import com.firefly.domain.lending.assetfinance.core.services.AssetFinanceService;
import org.fireflyframework.web.error.config.ErrorHandlingProperties;
import org.fireflyframework.web.error.converter.ExceptionConverterService;
import org.fireflyframework.web.error.service.ErrorResponseNegotiator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AssetFinanceController}.
 *
 * <p>Uses {@code @WebFluxTest} to test only the web layer, with the service mocked.
 */
@WebFluxTest(AssetFinanceController.class)
class AssetFinanceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AssetFinanceService assetFinanceService;

    // Required by GlobalExceptionHandler (org.fireflyframework.web v26.02.07)
    // Non-Optional constructor params not provided by the @WebFluxTest slice
    @MockBean
    private ExceptionConverterService exceptionConverterService;
    @MockBean
    private ErrorHandlingProperties errorHandlingProperties;
    @MockBean
    private ErrorResponseNegotiator errorResponseNegotiator;

    // ── Agreement tests ──────────────────────────────────────────────────────

    @Test
    void createAgreement_returns201() {
        var dto = new AssetFinanceAgreementDTO();
        when(assetFinanceService.createAgreement(any())).thenReturn(Mono.just(dto));

        webTestClient.post()
                .uri("/api/v1/asset-finance/agreements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AssetFinanceAgreementDTO.class);
    }

    @Test
    void getAgreement_returns200_whenFound() {
        var agreementId = UUID.randomUUID();
        var dto = new AssetFinanceAgreementDTO();
        when(assetFinanceService.getAgreement(agreementId)).thenReturn(Mono.just(dto));

        webTestClient.get()
                .uri("/api/v1/asset-finance/agreements/{id}", agreementId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AssetFinanceAgreementDTO.class);
    }

    @Test
    void getAgreement_returns404_whenNotFound() {
        var agreementId = UUID.randomUUID();
        when(assetFinanceService.getAgreement(agreementId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/asset-finance/agreements/{id}", agreementId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateAgreement_returns200_whenFound() {
        var agreementId = UUID.randomUUID();
        var dto = new AssetFinanceAgreementDTO();
        when(assetFinanceService.updateAgreement(eq(agreementId), any())).thenReturn(Mono.just(dto));

        webTestClient.put()
                .uri("/api/v1/asset-finance/agreements/{id}", agreementId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AssetFinanceAgreementDTO.class);
    }

    // ── Asset tests ──────────────────────────────────────────────────────────

    @Test
    void registerAsset_returns201() {
        var agreementId = UUID.randomUUID();
        var dto = new AssetFinanceAssetDTO();
        when(assetFinanceService.registerAsset(eq(agreementId), any())).thenReturn(Mono.just(dto));

        webTestClient.post()
                .uri("/api/v1/asset-finance/agreements/{agreementId}/assets", agreementId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AssetFinanceAssetDTO.class);
    }

    @Test
    void getAgreementAssets_returns200() {
        var agreementId = UUID.randomUUID();
        var page = new PaginationResponse();
        when(assetFinanceService.getAgreementAssets(agreementId)).thenReturn(Mono.just(page));

        webTestClient.get()
                .uri("/api/v1/asset-finance/agreements/{agreementId}/assets", agreementId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginationResponse.class);
    }

    @Test
    void getAsset_returns404_whenNotFound() {
        var agreementId = UUID.randomUUID();
        var assetId = UUID.randomUUID();
        when(assetFinanceService.getAsset(agreementId, assetId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/asset-finance/agreements/{agreementId}/assets/{assetId}", agreementId, assetId)
                .exchange()
                .expectStatus().isNotFound();
    }

    // ── End option tests ─────────────────────────────────────────────────────

    @Test
    void createEndOption_returns201() {
        var agreementId = UUID.randomUUID();
        var dto = new EndOptionDTO();
        when(assetFinanceService.createEndOption(eq(agreementId), any())).thenReturn(Mono.just(dto));

        webTestClient.post()
                .uri("/api/v1/asset-finance/agreements/{agreementId}/end-options", agreementId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EndOptionDTO.class);
    }

    @Test
    void getAgreementEndOptions_returns200() {
        var agreementId = UUID.randomUUID();
        var page = new PaginationResponse();
        when(assetFinanceService.getAgreementEndOptions(agreementId)).thenReturn(Mono.just(page));

        webTestClient.get()
                .uri("/api/v1/asset-finance/agreements/{agreementId}/end-options", agreementId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginationResponse.class);
    }

    @Test
    void updateEndOption_returns404_whenNotFound() {
        var agreementId = UUID.randomUUID();
        var optionId = UUID.randomUUID();
        var dto = new EndOptionDTO();
        when(assetFinanceService.updateEndOption(eq(agreementId), eq(optionId), any()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/v1/asset-finance/agreements/{agreementId}/end-options/{optionId}",
                        agreementId, optionId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound();
    }
}
