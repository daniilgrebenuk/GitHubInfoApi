package com.githubinfo.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateBean {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.defaultHeader("Authorization", "Bearer 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").build();
  }
}
