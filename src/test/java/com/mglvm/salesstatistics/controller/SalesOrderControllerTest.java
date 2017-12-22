package com.mglvm.salesstatistics.controller;

import com.mglvm.salesstatistics.model.dto.SalesOrder;
import com.mglvm.salesstatistics.service.SalesStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SalesOrderController.class)
public class SalesOrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SalesStatisticsService salesStatisticsService;

    @Test
    public void ShouldAcceptValidSalesOrderRequest() throws Exception {
        //Given
        SalesOrder salesOrder = new SalesOrder(50.0, 1513855109000l);
        doNothing().when(salesStatisticsService).recordSalesOrder(salesOrder);

        //When
        mvc.perform(post("/sales").param("sales_amount", "50.0")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isAccepted())
                .andExpect(content().bytes(new byte[0]));

        //Then
        verify(salesStatisticsService).recordSalesOrder(any());
    }

    @Test
    public void ShouldThrowExceptionForAnEmptySalesAmount() throws Exception {

        //When
        mvc.perform(post("/sales").param("sales_amount", "")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"errorCode\":400,\"errorMessage\":\"java.lang.NumberFormatException: empty String\"}"));
    }

    @Test
    public void ShouldThrowExceptionForAnInvalidSalesAmount() throws Exception {

        //When
        mvc.perform(post("/sales").param("sales_amount", "invalid_sales_amount")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"errorCode\":400,\"errorMessage\":\"java.lang.NumberFormatException: For input string: \\\"invalid_sales_amount\\\"\"}"));

    }
}
