package com.mglvm.salesstatistics.controller;

import com.mglvm.salesstatistics.model.dto.SalesOrder;
import com.mglvm.salesstatistics.service.SalesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static com.mglvm.salesstatistics.util.TimeUtil.getCurrentTime;

@RestController
public class SalesOrderController {

    private final SalesStatisticsService salesStatisticsService;

    @Autowired
    public SalesOrderController(SalesStatisticsService salesStatisticsService) {
        this.salesStatisticsService = salesStatisticsService;
    }

    @PostMapping(value = "/sales", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void recordSalesOrder(@RequestParam(name = "sales_amount", required = true) String salesAmount, HttpServletResponse response) {
        SalesOrder salesOrder = new SalesOrder(convertSalesAmount(salesAmount), getSalesOrderEventTimestamp());
        salesStatisticsService.recordSalesOrder(salesOrder);
        response.setStatus(HttpStatus.ACCEPTED.value());
    }

    private long getSalesOrderEventTimestamp() {
        return getCurrentTime();
    }

    private Double convertSalesAmount(String salesAmount) {
        try {
            return Double.parseDouble(salesAmount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
