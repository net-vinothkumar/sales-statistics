package com.mglvm.salesstatistics.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"total_sales_amount", "average_amount_per_order"})
public final class SalesStatisticsDTO {

    @JsonProperty(value = "total_sales_amount")
    private final String totalSalesAmount;

    @JsonProperty(value = "average_amount_per_order")
    private final String averageAmountPerOrder;

    public SalesStatisticsDTO(String totalSalesAmount, String averageAmountPerOrder) {
        this.totalSalesAmount = totalSalesAmount;
        this.averageAmountPerOrder = averageAmountPerOrder;
    }

    public String getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public String getAverageAmountPerOrder() {
        return averageAmountPerOrder;
    }
}
