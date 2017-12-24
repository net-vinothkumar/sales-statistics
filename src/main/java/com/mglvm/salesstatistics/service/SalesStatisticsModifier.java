package com.mglvm.salesstatistics.service;

@FunctionalInterface
public interface SalesStatisticsModifier<SalesOrder> {
    public void modifySalesStatistics(SalesOrder salesOrder);
}
