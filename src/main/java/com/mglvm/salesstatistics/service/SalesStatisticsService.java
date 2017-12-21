package com.mglvm.salesstatistics.service;

import com.mglvm.salesstatistics.model.dto.SalesOrder;
import com.mglvm.salesstatistics.model.dto.SalesStatistics;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class SalesStatisticsService {

    private static volatile ConcurrentSkipListSet<SalesOrder> salesOrdersList;
    private static volatile SalesStatistics salesStatistics;
    private static volatile long latesSalesOrderTimestamp;

    public SalesStatisticsService() {
        salesOrdersList = new ConcurrentSkipListSet<>();
        salesStatistics = new SalesStatistics(0.00, 0.00);
    }

    /**
     * 1.Remove old sales order from the list
     * 2.Reduce the amount of removed old sales order from total amount
     * 3.Add the new sales order
     * 4.Add the sales order amount to total amount
     * @param salesOrderInput
     */
    public void recordSalesOrder(SalesOrder salesOrderInput) {

        clearSalesOrderOlderThanOneMinute();

        if (salesOrderInput.getTimestamp() >= getLastOneMinute()) {
            salesOrdersList.add(salesOrderInput);
            Double totalSalesAmount = salesStatistics.getTotalSalesAmount() + salesOrderInput.getAmount();
            salesStatistics.setTotalSalesAmount(totalSalesAmount);
            Double averageAmountPerOrder = totalSalesAmount / salesOrdersList.size();
            salesStatistics.setAverageAmountPerOrder(averageAmountPerOrder);
            latesSalesOrderTimestamp = salesOrderInput.getTimestamp();
        }
    }

    private void clearSalesOrderOlderThanOneMinute() {
        salesOrdersList.stream()
                .filter(salesOrder -> salesOrder.getTimestamp() < getLastOneMinute())
                .forEach(oldSalesOrder -> {
                            salesOrdersList.remove(oldSalesOrder);
                            salesStatistics.setTotalSalesAmount(reduceOldSalesOrderAmount(oldSalesOrder));
                        }
                );
    }

    private double reduceOldSalesOrderAmount(SalesOrder recentSalesOrder) {
        return salesStatistics.getTotalSalesAmount() - recentSalesOrder.getAmount();
    }

    public SalesStatistics getSalesStatistics() {
        if (latesSalesOrderTimestamp <= getLastOneMinute()) {
            return noSalesOrderForLastOneMinute();
        }
        return salesStatistics;
    }

    private SalesStatistics noSalesOrderForLastOneMinute() {
        return new SalesStatistics(0.00, 0.00);
    }

    private long getLastOneMinute() {
        return (Instant.now().getEpochSecond() * 1000) - 60000;
    }
}
