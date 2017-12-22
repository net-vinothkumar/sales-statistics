package com.mglvm.salesstatistics.integrationtest;

import com.mglvm.salesstatistics.model.dto.SalesStatisticsDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SalesStatisticsIntegrationTest {

    @Value("${local.server.port}")
    private int localServerPort;

    RestTemplate testRestTemplate = null;

    @Before
    public void setUp(){
        testRestTemplate = new RestTemplate();
    }

    @Test
    public void ShouldGetValidSalesStatisticsWhenThereAreClientRequestsConcurrently() throws InterruptedException {

        //Given
        HttpEntity entity = prepareHttpHeaders();

        //When
        IntStream.range(0, 100)
                .parallel()
                .forEach(salesTriggered -> {
                    ResponseEntity<Void> recordSalesOrderResponse = testRestTemplate
                            .exchange("http://localhost:{localServerPort}/sales?sales_amount=100", HttpMethod.POST,
                                    null, Void.class, localServerPort);
                    assertThat(recordSalesOrderResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
                });

        //Then
        ResponseEntity<SalesStatisticsDTO> getSalesStatisticsResponse = testRestTemplate
                .exchange("http://localhost:{localServerPort}/statistics", HttpMethod.GET,
                        entity, SalesStatisticsDTO.class, localServerPort);

        assertThat(getSalesStatisticsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getSalesStatisticsResponse.getBody().getTotalSalesAmount()).isEqualTo("10000.00");
        assertThat(getSalesStatisticsResponse.getBody().getAverageAmountPerOrder()).isEqualTo("100.00");

        mockNoSalesForSomeTime();

        getSalesStatisticsResponse = testRestTemplate.exchange("http://localhost:{localServerPort}/statistics", HttpMethod.GET,
                        entity, SalesStatisticsDTO.class, localServerPort);

        assertThat(getSalesStatisticsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getSalesStatisticsResponse.getBody().getTotalSalesAmount()).isEqualTo("0.00");
        assertThat(getSalesStatisticsResponse.getBody().getAverageAmountPerOrder()).isEqualTo("0.00");
    }

    private HttpEntity prepareHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        return new HttpEntity(headers);
    }

    private void mockNoSalesForSomeTime() throws InterruptedException {
        Thread.sleep(70000);
    }
}
