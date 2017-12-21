package com.mglvm.salesstatistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mglvm.salesstatistics.model.dto.SalesStatistics;
import com.mglvm.salesstatistics.model.dto.SalesStatisticsDTO;
import com.mglvm.salesstatistics.service.SalesStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SalesStatisticsController.class)
public class SalesStatisticsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SalesStatisticsService salesStatisticsService;

    @Test
    public void ShouldGetSalesStatistics() throws Exception {

        //Given
        SalesStatistics mockSalesStatistics = new SalesStatistics(200.00,100.00);
        when(salesStatisticsService.getSalesStatistics()).thenReturn(mockSalesStatistics);

        //When
        mvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(new SalesStatisticsDTO("200.00", "100.00"))));

        //Then
        verify(salesStatisticsService).getSalesStatistics();
    }

    private String toJson(SalesStatisticsDTO salesStatisticsDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(salesStatisticsDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
