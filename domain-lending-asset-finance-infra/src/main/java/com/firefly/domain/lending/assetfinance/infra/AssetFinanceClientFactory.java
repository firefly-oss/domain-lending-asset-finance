package com.firefly.domain.lending.assetfinance.infra;

import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAgreementApi;
import com.firefly.core.lending.assetfinance.sdk.api.AssetFinanceAssetApi;
import com.firefly.core.lending.assetfinance.sdk.api.DeliveryRecordApi;
import com.firefly.core.lending.assetfinance.sdk.api.EndOptionApi;
import com.firefly.core.lending.assetfinance.sdk.api.PickupRecordApi;
import com.firefly.core.lending.assetfinance.sdk.api.ReturnRecordApi;
import com.firefly.core.lending.assetfinance.sdk.api.ServiceEventApi;
import com.firefly.core.lending.assetfinance.sdk.api.UsageRecordApi;
import com.firefly.core.lending.assetfinance.sdk.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Creates and exposes WebClient-based API clients for {@code core-lending-asset-finance}.
 *
 * <p>Uses {@code @Component} (not {@code @Configuration}) per banking platform convention.
 * Base path is injected via {@link AssetFinanceProperties}.
 */
@Component
public class AssetFinanceClientFactory {

    private final ApiClient apiClient;

    /**
     * Creates a new factory and configures the shared {@link ApiClient} with the base path from properties.
     *
     * @param properties the asset finance configuration properties
     */
    public AssetFinanceClientFactory(AssetFinanceProperties properties) {
        this.apiClient = new ApiClient();
        this.apiClient.setBasePath(properties.getBasePath());
    }

    /**
     * Creates the {@link AssetFinanceAgreementApi} client bean.
     *
     * @return the agreement API client
     */
    @Bean
    public AssetFinanceAgreementApi assetFinanceAgreementApi() {
        return new AssetFinanceAgreementApi(apiClient);
    }

    /**
     * Creates the {@link AssetFinanceAssetApi} client bean.
     *
     * @return the asset API client
     */
    @Bean
    public AssetFinanceAssetApi assetFinanceAssetApi() {
        return new AssetFinanceAssetApi(apiClient);
    }

    /**
     * Creates the {@link DeliveryRecordApi} client bean.
     *
     * @return the delivery record API client
     */
    @Bean
    public DeliveryRecordApi deliveryRecordApi() {
        return new DeliveryRecordApi(apiClient);
    }

    /**
     * Creates the {@link PickupRecordApi} client bean.
     *
     * @return the pickup record API client
     */
    @Bean
    public PickupRecordApi pickupRecordApi() {
        return new PickupRecordApi(apiClient);
    }

    /**
     * Creates the {@link ReturnRecordApi} client bean.
     *
     * @return the return record API client
     */
    @Bean
    public ReturnRecordApi returnRecordApi() {
        return new ReturnRecordApi(apiClient);
    }

    /**
     * Creates the {@link ServiceEventApi} client bean.
     *
     * @return the service event API client
     */
    @Bean
    public ServiceEventApi serviceEventApi() {
        return new ServiceEventApi(apiClient);
    }

    /**
     * Creates the {@link UsageRecordApi} client bean.
     *
     * @return the usage record API client
     */
    @Bean
    public UsageRecordApi usageRecordApi() {
        return new UsageRecordApi(apiClient);
    }

    /**
     * Creates the {@link EndOptionApi} client bean.
     *
     * @return the end option API client
     */
    @Bean
    public EndOptionApi endOptionApi() {
        return new EndOptionApi(apiClient);
    }
}
