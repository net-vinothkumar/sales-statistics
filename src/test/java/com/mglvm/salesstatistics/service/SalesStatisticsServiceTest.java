package com.mglvm.salesstatistics.service;

import com.mglvm.salesstatistics.model.dto.SalesOrder;
import com.mglvm.salesstatistics.model.dto.SalesStatistics;
import com.mglvm.salesstatistics.model.dto.SalesStatisticsDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SalesStatisticsServiceTest {

    @Test
    public void ShouldRecordSalesOrderForTheGivenValidSalesAmount() {

        //Given
        SalesStatisticsService salesStatisticsService = new SalesStatisticsService();
        long salesOrderTimestamp = Instant.now().getEpochSecond() * 1000;
        SalesOrder salesOrder = new SalesOrder(100.00, salesOrderTimestamp);

        //When
        salesStatisticsService.recordSalesOrder(salesOrder);

        //Then
        assertThat(salesStatisticsService.getSalesStatistics().getTotalSalesAmount()).isEqualTo(100);
        assertThat(salesStatisticsService.getSalesStatistics().getAverageAmountPerOrder()).isEqualTo(100);
    }

    @Test
    public void ShouldGetTheSalesStatisticsForLastOneMinute() throws InterruptedException {

        //Given
        SalesStatisticsService salesStatisticsService = new SalesStatisticsService();
        long salesOrderTimestamp_1 = Instant.now().getEpochSecond() * 1000;
        long salesOrderTimestamp_2 = Instant.now().getEpochSecond() * 1000;

        long TWO_MINUTE_DELAY = 120000;

        long oldSalesOrderTimestamp = (Instant.now().getEpochSecond() * 1000) - TWO_MINUTE_DELAY;

        List<SalesOrder> mockSalesOrderList = Arrays.asList(new SalesOrder(100.00, salesOrderTimestamp_1),
                new SalesOrder(200.00, salesOrderTimestamp_2),
                new SalesOrder(300.00, oldSalesOrderTimestamp));

        mockSalesOrderList.forEach(salesOrder -> salesStatisticsService.recordSalesOrder(salesOrder));
        SalesStatistics salesStatistics = salesStatisticsService.getSalesStatistics();

        //When
        assertThat(salesStatistics.getTotalSalesAmount()).isEqualTo(300.00);
        assertThat(salesStatistics.getAverageAmountPerOrder()).isEqualTo(150.00);
    }

    @Test
    public void ShouldGetTheSalesStatisticsForLastOneMinuteWhenThereAreOldSalesOrder() throws InterruptedException {

        //Given
        SalesStatisticsService salesStatisticsService = new SalesStatisticsService();
        long salesOrderTimpeStamp_1 = Instant.now().getEpochSecond() * 1000;
        long salesOrderTimpeStamp_2 = Instant.now().getEpochSecond() * 1000;

        List<SalesOrder> mockSalesOrderList = Arrays.asList(new SalesOrder(100.00, salesOrderTimpeStamp_1),
                new SalesOrder(200.00, salesOrderTimpeStamp_2));

        mockSalesOrderList.forEach(salesOrder -> salesStatisticsService.recordSalesOrder(salesOrder));
        SalesStatistics salesStatistics = salesStatisticsService.getSalesStatistics();

        assertThat(salesStatistics.getTotalSalesAmount()).isEqualTo(300.00);
        assertThat(salesStatistics.getAverageAmountPerOrder()).isEqualTo(150.00);

        //When
        mockNoSalesForSometime();
        long salesOrderTimpeStamp = Instant.now().getEpochSecond() * 1000;
        salesStatisticsService.recordSalesOrder(new SalesOrder(100.00, salesOrderTimpeStamp));
        salesStatistics = salesStatisticsService.getSalesStatistics();

        //Then
        assertThat(salesStatistics.getTotalSalesAmount()).isEqualTo(100.00);
        assertThat(salesStatistics.getAverageAmountPerOrder()).isEqualTo(100.00);
    }

    private void mockNoSalesForSometime() throws InterruptedException {
        Thread.sleep(70000);
    }
}
