package com.mglvm.salesstatistics.configuration;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Vinoth Kumar on 21/12/2017.
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfiguration {

    @Bean
    public Docket analyticsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mglvm.salesstatistics.controller"))
                .paths(regex(".*"))
                .build()
                .apiInfo(apiInformation());
    }

    private ApiInfo apiInformation() {
        return  new ApiInfo(
                "Spring Boot REST API - Sales Statistics",
                "Spring Boot REST API Demo",
                "0.0.1",
                "Terms of service",
                new Contact("Vinoth Kumar Dhanasekar", "", "net.vinothkumar@gmail.com"),
                "",
                "");
    }
}
