package com.visa.samplecode.config;

import com.visa.samplecode.utils.XPayTokenUtil;
import feign.RequestInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PlanAcceptancesApiConfiguration {

  @Value("${client.visa.apiKey}")
  private String apiKey;

  @Value("${client.visa.sharedSecret}")
  private String sharedSecret;

  @Value("${client.visa.resourcePaths.planAcceptancesApi}")
  private String resourcePath;

  @Bean
  public RequestInterceptor requestInterceptor() {
    String queryString = "apiKey=" + apiKey;
    return requestTemplate -> {
      String xPayToken = "";
      String requestBody = new String(requestTemplate.body(), StandardCharsets.UTF_8);
      try {
        xPayToken = XPayTokenUtil.generateXpaytoken(resourcePath, queryString, requestBody, sharedSecret);
      } catch (SignatureException e) {
        e.printStackTrace();
      }
      log.info("PlanAcceptancesApi -> X-PAY-TOKEN:" + xPayToken);
      requestTemplate.header("Accept", ContentType.APPLICATION_JSON.getMimeType());
      requestTemplate.header("X-PAY-TOKEN", xPayToken);
    };
  }
}
