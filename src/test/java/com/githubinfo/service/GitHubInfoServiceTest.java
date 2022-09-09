package com.githubinfo.service;

import com.githubinfo.constant.GitHubUtilConstants;
import com.githubinfo.converter.GitHubDtoConverter;
import com.githubinfo.dto.BranchDto;
import com.githubinfo.dto.RepositoryDto;
import com.githubinfo.dto.githubapi.ApiGitHubBranchDto;
import com.githubinfo.dto.githubapi.ApiGitHubCommitDto;
import com.githubinfo.dto.githubapi.ApiGitHubOwnerDto;
import com.githubinfo.dto.githubapi.ApiGitHubRepositoryDto;
import com.githubinfo.service.impl.GitHubInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubInfoServiceTest {

  private GitHubInfoService gitHubInfoService;

  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    gitHubInfoService = new GitHubInfoServiceImpl(restTemplate, new GitHubDtoConverter());
  }

  @Test
  void returnEmptyList() {
    ResponseEntity<ApiGitHubRepositoryDto[]> response = ResponseEntity.ok(new ApiGitHubRepositoryDto[0]);

    when(restTemplate.getForEntity(any(String.class), eq(ApiGitHubRepositoryDto[].class))).thenReturn(response);

    List<RepositoryDto> toVerify = gitHubInfoService.findGitHubInfoByUsername("");

    assertAll(() -> assertThat(toVerify).isNotNull(), () -> assertThat(toVerify).hasSize(0));
  }

  @Test
  void returnOneRepositoryWithNoBranches() {
    ResponseEntity<ApiGitHubRepositoryDto[]> repositories = ResponseEntity.ok(new ApiGitHubRepositoryDto[]{
        new ApiGitHubRepositoryDto("1", new ApiGitHubOwnerDto("1"), false)
    });

    ResponseEntity<ApiGitHubBranchDto[]> branches = ResponseEntity.ok(new ApiGitHubBranchDto[0]);

    when(restTemplate.getForEntity(any(String.class), eq(ApiGitHubRepositoryDto[].class))).thenReturn(repositories);
    when(restTemplate.getForEntity(any(String.class), eq(ApiGitHubBranchDto[].class))).thenReturn(branches);

    List<RepositoryDto> toVerify = gitHubInfoService.findGitHubInfoByUsername("");

    assertAll(
        () -> assertThat(toVerify).hasSize(1),
        () -> assertThat(toVerify.get(0).branches()).hasSize(0)
    );
  }

  @Test
  void returnTwoRepositoryEachWithFewBranchesAndOneFork() {
    ResponseEntity<ApiGitHubRepositoryDto[]> repositories = ResponseEntity.ok(new ApiGitHubRepositoryDto[]{
        new ApiGitHubRepositoryDto("first", new ApiGitHubOwnerDto("first"), false),
        new ApiGitHubRepositoryDto("second", new ApiGitHubOwnerDto("second"), false),
        new ApiGitHubRepositoryDto("3", new ApiGitHubOwnerDto("3"), true)
    });

    ResponseEntity<ApiGitHubBranchDto[]> firstRepositoryBranches = ResponseEntity.ok(new ApiGitHubBranchDto[]{
        new ApiGitHubBranchDto("1", new ApiGitHubCommitDto("1")),
        new ApiGitHubBranchDto("2", new ApiGitHubCommitDto("2"))
    });
    ResponseEntity<ApiGitHubBranchDto[]> secondRepositoryBranches = ResponseEntity.ok(new ApiGitHubBranchDto[]{
        new ApiGitHubBranchDto("3", new ApiGitHubCommitDto("3"))
    });

    RepositoryDto firstRepositoryExpected = new RepositoryDto("first", "first", List.of(
        new BranchDto("1", "1"),
        new BranchDto("2", "2")
    ));

    RepositoryDto secondRepositoryExpected = new RepositoryDto("second", "second", List.of(
        new BranchDto("3", "3")
    ));

    when(restTemplate.getForEntity(any(String.class), eq(ApiGitHubRepositoryDto[].class))).thenReturn(repositories);
    when(restTemplate.getForEntity(contains("first"), eq(ApiGitHubBranchDto[].class))).thenReturn(firstRepositoryBranches);
    when(restTemplate.getForEntity(contains("second"), eq(ApiGitHubBranchDto[].class))).thenReturn(secondRepositoryBranches);

    List<RepositoryDto> toVerify = gitHubInfoService.findGitHubInfoByUsername("");

    assertThat(toVerify).hasSize(2).contains(firstRepositoryExpected, secondRepositoryExpected);
  }

  @Test
  void returnRepositoriesThatHaveMoreThanAmountOfElementsOnOnePage() {
    ApiGitHubRepositoryDto[] repositories = IntStream
        .range(1, (int) (1.3 * GitHubUtilConstants.AMOUNT_OF_REPOSITORIES_ON_PAGE))
        .mapToObj(i -> new ApiGitHubRepositoryDto("" + i, new ApiGitHubOwnerDto("" + i), false))
        .toArray(ApiGitHubRepositoryDto[]::new);

    ResponseEntity<ApiGitHubRepositoryDto[]> repositoriesResponse = ResponseEntity.ok(repositories);

    ResponseEntity<ApiGitHubBranchDto[]> branchesResponse = ResponseEntity.ok(new ApiGitHubBranchDto[0]);

    when(restTemplate.getForEntity(any(String.class), eq(ApiGitHubRepositoryDto[].class))).thenReturn(repositoriesResponse);
    when(restTemplate.getForEntity(any(String.class), eq(ApiGitHubBranchDto[].class))).thenReturn(branchesResponse);

    List<RepositoryDto> toVerify = gitHubInfoService.findGitHubInfoByUsername("");

    assertThat(toVerify).hasSize(repositories.length);
  }
}