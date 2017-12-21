package com.mglvm.salesstatistics.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SalesStatisticsDTO {

    @JsonProperty(value = "total_sales_amount")
    private String totalSalesAmount;

    @JsonProperty(value = "average_amount_per_order")
    private String averageAmountPerOrder;

    public SalesStatisticsDTO(String totalSalesAmount, String averageAmountPerOrder) {
        this.totalSalesAmount = totalSalesAmount;
        this.averageAmountPerOrder = averageAmountPerOrder;
    }

    public void setTotalSalesAmount(String totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public void setAverageAmountPerOrder(String averageAmountPerOrder) {
        this.averageAmountPerOrder = averageAmountPerOrder;
    }

    public String getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public String getAverageAmountPerOrder() {
        return averageAmountPerOrder;
    }
}
