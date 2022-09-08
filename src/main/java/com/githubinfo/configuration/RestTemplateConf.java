package com.githubinfo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConf {

  @Value("${github-authorization-token}")
  private String gitHubAuthorizationToken;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.defaultHeader("Authorization", String.format("Bearer %s", gitHubAuthorizationToken)).build();
  }
}
