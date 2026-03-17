package com.firefly.domain.lending.assetfinance.infra;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the core-lending-asset-finance downstream service.
 * Bound from {@code api-configuration.core-lending.asset-finance} in application.yaml.
 *
 * <p>No {@code @Configuration} annotation — the application class carries
 * {@code @ConfigurationPropertiesScan} to register all {@code @ConfigurationProperties} beans.
 */
@ConfigurationProperties(prefix = "api-configuration.core-lending.asset-finance")
@Data
public class AssetFinanceProperties {

    private String basePath;
}
