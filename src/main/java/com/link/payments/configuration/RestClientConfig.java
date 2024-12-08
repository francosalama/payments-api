package com.link.payments.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

   @Value("${exchange-rate-api.base-url}")
   private String exchangeRateApiUrl;

   @Bean
   public RestClient exchangeRateApiRestClient() {
      return RestClient.builder().baseUrl(exchangeRateApiUrl).build();
   }

}
