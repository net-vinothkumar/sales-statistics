package com.mglvm.salesstatistics.service;

import com.mglvm.salesstatistics.model.dto.SalesOrder;
import com.mglvm.salesstatistics.model.dto.SalesStatistics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static com.mglvm.salesstatistics.util.TimeUtil.getCurrentTime;

/**
 * Created by Vinoth Kumar on 21/12/2017.
 * Record the sales order and update the sales statistics.
 */
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
     *
     * @param newSalesOrder
     */
    @Async
    public void recordSalesOrder(SalesOrder newSalesOrder) {

        clearOldSalesOrders();

        if (newSalesOrder.getTimestamp() >= getLastOneMinute()) {
            salesOrdersList.add(newSalesOrder);
            latesSalesOrderTimestamp = newSalesOrder.getTimestamp();

            updateSalesStatisticsAtomically(newSalesOrder, salesOrderInput -> {
                salesStatistics.setTotalSalesAmount(salesStatistics.getTotalSalesAmount() + salesOrderInput.getAmount());
                salesStatistics.setAverageAmountPerOrder(salesStatistics.getTotalSalesAmount() / salesOrdersList.size());
            });
        }
    }

    private void clearOldSalesOrders() {
        salesOrdersList.stream()
                .filter(salesOrder -> salesOrder.getTimestamp() < getLastOneMinute())
                .forEach(filteredOldSalesOrder -> {
                            salesOrdersList.remove(filteredOldSalesOrder);

                            updateSalesStatisticsAtomically(filteredOldSalesOrder, salesOrderInput -> {
                                salesStatistics.setTotalSalesAmount(salesStatistics.getTotalSalesAmount() - salesOrderInput.getAmount());
                                salesStatistics.setAverageAmountPerOrder(salesStatistics.getTotalSalesAmount() / salesOrdersList.size());
                            });
                        }
                );
    }

    private void updateSalesStatisticsAtomically(SalesOrder salesOrder, SalesStatisticsModifier<SalesOrder> modifier) {
        lock.lock();
        try {
            modifier.modifySalesStatistics(salesOrder);
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
        return getCurrentTime() - LAST_ONE_MINUTE;
    }

}
