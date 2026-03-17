package com.firefly.domain.lending.assetfinance.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Domain-level DTO representing a summary of an asset finance agreement,
 * composed from {@code core-lending-asset-finance} data for experience-layer clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Asset finance agreement summary for domain-level consumers")
public class AssetFinanceAgreementSummaryDTO {

    @Schema(description = "Agreement identifier")
    private UUID assetFinanceAgreementId;

    @Schema(description = "Loan servicing case this agreement is linked to")
    private UUID loanServicingCaseId;

    @Schema(description = "Finance type: RENTING or LEASING")
    private String financeType;

    @Schema(description = "Current agreement status")
    private String agreementStatus;

    @Schema(description = "Contract start date")
    private LocalDate startDate;

    @Schema(description = "Contract end date")
    private LocalDate endDate;

    @Schema(description = "Total contract value")
    private BigDecimal totalValue;
}
