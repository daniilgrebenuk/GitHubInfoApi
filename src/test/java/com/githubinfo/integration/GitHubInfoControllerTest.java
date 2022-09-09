package com.githubinfo.integration;

import com.githubinfo.dto.RepositoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubInfoControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void getUserWithOnlyFork() {
    String username = "Tiltman";

    assertThat(restTemplate.getForEntity(String.format("http://localhost:%d/api/github/user?username=%s", port, username), RepositoryDto[].class).getBody())
        .hasSize(0);
  }

  @Test
  void getUserInfo() {
    String username = "daniilgrebenuk";

    assertThat(restTemplate.getForEntity(String.format("http://localhost:%d/api/github/user?username=%s", port, username), String.class).getBody())
        .contains("{\"repositoryName\":\"SpringbootSupermarket\",\"ownerLogin\":\"daniilgrebenuk\",\"branches\":[{\"name\":\"master\",\"lastCommitSha\":\"caa6a0261ac9a1a0b7e00acb57366aee0e7c437f\"}]}");
  }

  @Test
  void getUserInfoWithUsernameThatDoesntExist() {
    String username = "daniilgrebenuk1231232121321";

    ResponseEntity<String> response = restTemplate.getForEntity(String.format("http://localhost:%d/api/github/user?username=%s", port, username), String.class);

    assertAll(
        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
        () -> assertThat(response.getBody())
            .contains(String.format("\"Message\":\"User with username:'%s' does not exist!\"", username))
            .contains("\"status\":404")
    );

  }
}