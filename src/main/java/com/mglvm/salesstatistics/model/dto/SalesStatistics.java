package com.mglvm.salesstatistics.model.dto;
/**
 * Created by Vinoth Kumar on 21/12/2017.
 */
public final class SalesStatistics {

    private Double totalSalesAmount;

    private Double averageAmountPerOrder;

    public SalesStatistics(Double totalSalesAmount, Double averageAmountPerOrder) {
        this.totalSalesAmount = totalSalesAmount;
        this.averageAmountPerOrder = averageAmountPerOrder;
    }

    public void setTotalSalesAmount(Double totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public void setAverageAmountPerOrder(Double averageAmountPerOrder) {
        this.averageAmountPerOrder = averageAmountPerOrder;
    }

    public Double getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public Double getAverageAmountPerOrder() {
        return averageAmountPerOrder;
    }
}
