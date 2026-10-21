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


package io.fireflysoftware.domain.lending.assetfinance.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.fireflysoftware.domain.lending.assetfinance.interfaces.enums.FinanceType;
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

    @Schema(description = "Finance type: LEASING (financial lease with a purchase option) or RENTING (operating rental without a purchase option)")
    private FinanceType financeType;

    @Schema(description = "Current agreement status")
    private String agreementStatus;

    @Schema(description = "Contract start date")
    private LocalDate startDate;

    @Schema(description = "Contract end date")
    private LocalDate endDate;

    @Schema(description = "Total contract value")
    private BigDecimal totalValue;
}
