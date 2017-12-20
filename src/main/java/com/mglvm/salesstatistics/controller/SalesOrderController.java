package com.mglvm.salesstatistics.controller;

import com.mglvm.salesstatistics.model.dto.SalesOrder;
import com.mglvm.salesstatistics.service.SalesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class SalesOrderController {

    private final SalesStatisticsService salesStatisticsService;

    @Autowired
    public SalesOrderController(SalesStatisticsService salesStatisticsService) {
        this.salesStatisticsService = salesStatisticsService;
    }

    @PostMapping(value = "/sales", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void recordSalesOrder(@RequestParam(name = "sales_amount", required = true) String salesAmount){
        SalesOrder salesOrder = new SalesOrder(Double.parseDouble(salesAmount));
        salesStatisticsService.recordSalesOrder(salesOrder);
    }
}
