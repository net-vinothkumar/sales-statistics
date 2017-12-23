package com.mglvm.salesstatistics.service;

import com.mglvm.salesstatistics.model.dto.SalesOrder;
import com.mglvm.salesstatistics.model.dto.SalesStatistics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.mglvm.salesstatistics.util.TimeUtil.getCurrentTime;

@Service
public class SalesStatisticsService {

    private static volatile ConcurrentSkipListSet<SalesOrder> salesOrdersList;
    private static volatile SalesStatistics salesStatistics;
    private static volatile long latesSalesOrderTimestamp;
    private final Lock lock = new ReentrantLock(true);

    private long LAST_ONE_MINUTE = 60000;

    public SalesStatisticsService() {
        salesOrdersList = new ConcurrentSkipListSet<>();
        salesStatistics = new SalesStatistics(0.00, 0.00);
    }

    /**
     * Method to add the new sales order to sales statistics.
     * 1.Remove old sales order from the list
     * 2.Reduce the amount of removed old sales order from total amount,calculate average amount per order
     * 3.Add the new sales order
     * 4.Add the sales order amount to total amount,calculate average amount per order
     * @param salesOrderInput
     */
    @Async
    public void recordSalesOrder(SalesOrder salesOrderInput) {

        clearOldSalesOrders();

        if (salesOrderInput.getTimestamp() >= getLastOneMinute()) {
            salesOrdersList.add(salesOrderInput);
            latesSalesOrderTimestamp = salesOrderInput.getTimestamp();
            addNewSalesOrderToSalesStatistics(salesOrderInput);
        }
    }

    private void addNewSalesOrderToSalesStatistics(SalesOrder salesOrderInput) {
        lock.lock();
        try {
            Double totalSalesAmount = salesStatistics.getTotalSalesAmount() + salesOrderInput.getAmount();
            Double averageAmountPerOrder = totalSalesAmount / salesOrdersList.size();

            salesStatistics.setTotalSalesAmount(totalSalesAmount);
            salesStatistics.setAverageAmountPerOrder(averageAmountPerOrder);
        } finally {
            lock.unlock();
        }
    }

    private void clearOldSalesOrders() {
        salesOrdersList.stream()
                .filter(salesOrder -> salesOrder.getTimestamp() < getLastOneMinute())
                .forEach(oldSalesOrder -> {
                            salesOrdersList.remove(oldSalesOrder);
                            removeOldSalesOrderFromSalesStatistics(oldSalesOrder);
                        }
                );
    }

    private void removeOldSalesOrderFromSalesStatistics(SalesOrder recentSalesOrder) {
        lock.lock();
        try {
            salesStatistics.setTotalSalesAmount(salesStatistics.getTotalSalesAmount() - recentSalesOrder.getAmount());
            salesStatistics.setAverageAmountPerOrder(salesStatistics.getTotalSalesAmount() / salesOrdersList.size());
        } finally {
            lock.unlock();
        }
    }

    public SalesStatistics getSalesStatistics() {
        if (latesSalesOrderTimestamp <= getLastOneMinute()) {
            return zeroSalesOrderForLastOneMinute();
        }
        return salesStatistics;
    }

    private SalesStatistics zeroSalesOrderForLastOneMinute() {
        return new SalesStatistics(0.00, 0.00);
    }

    private long getLastOneMinute() {
        return getCurrentTime() - LAST_ONE_MINUTE ;
    }
}
