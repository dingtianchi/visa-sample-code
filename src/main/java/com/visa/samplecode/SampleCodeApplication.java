package com.visa.samplecode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SampleCodeApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleCodeApplication.class, args);
  }
}
