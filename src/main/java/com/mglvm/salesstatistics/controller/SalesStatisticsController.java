package com.mglvm.salesstatistics.controller;

import com.mglvm.salesstatistics.model.dto.SalesStatistics;
import com.mglvm.salesstatistics.model.dto.SalesStatisticsDTO;
import com.mglvm.salesstatistics.service.SalesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;
/**
 * Created by Vinoth Kumar on 21/12/2017.
 */
@RestController
public class SalesStatisticsController {
    private static final String DOUBLE_WITH_TWO_DECIMAL_POINTS = "%.2f";

    private final SalesStatisticsService salesStatisticsService;

    @Autowired
    public SalesStatisticsController(SalesStatisticsService salesStatisticsService) {
        this.salesStatisticsService = salesStatisticsService;
    }

    @GetMapping(value = "/statistics")
    public SalesStatisticsDTO getSalesStatistics() {

        SalesStatistics salesStatistics = salesStatisticsService.getSalesStatistics();

        SalesStatisticsDTO salesStatisticsDTO = new SalesStatisticsDTO(format(DOUBLE_WITH_TWO_DECIMAL_POINTS, salesStatistics.getTotalSalesAmount()),
                                                                       format(DOUBLE_WITH_TWO_DECIMAL_POINTS, salesStatistics.getAverageAmountPerOrder()));

        return salesStatisticsDTO;
    }
}
