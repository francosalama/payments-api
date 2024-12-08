package com.link.payments.client;

import com.link.payments.dto.external.ExchangeRateApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ExchangeRateApiClient {

   private static final String GET_EXCHANGE_RATE_PATH = "/{apiKey}/pair/{baseCurrency}/USD/{amount}";

   private final RestClient exchangeRateApiRestClient;

   @Value("${exchange-rate-api.api-key}")
   private String apiKey;

   public ExchangeRateApiResponse getExchangeRate(String baseCurrency, BigDecimal amount) {
      return exchangeRateApiRestClient.get().uri(GET_EXCHANGE_RATE_PATH, apiKey, baseCurrency, amount).retrieve().body(ExchangeRateApiResponse.class);
   }

}
