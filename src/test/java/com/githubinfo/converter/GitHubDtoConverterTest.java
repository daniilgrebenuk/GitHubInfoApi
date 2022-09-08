package com.githubinfo.converter;

import com.githubinfo.dto.BranchDto;
import com.githubinfo.dto.RepositoryDto;
import com.githubinfo.dto.githubapi.ApiGitHubBranchDto;
import com.githubinfo.dto.githubapi.ApiGitHubCommitDto;
import com.githubinfo.dto.githubapi.ApiGitHubOwnerDto;
import com.githubinfo.dto.githubapi.ApiGitHubRepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class GitHubDtoConverterTest {

  private GitHubDtoConverter gitHubDtoConverter;

  @BeforeEach
  void setUp() {
    gitHubDtoConverter = new GitHubDtoConverter();
  }

  @Test
  void mapOfApiGitHubRepositoryDtoAndApiGitHubBranchDtosToListOfRepositoryDto() {
    Map<ApiGitHubRepositoryDto, List<ApiGitHubBranchDto>> repositories = new LinkedHashMap<>();
    repositories.put(new ApiGitHubRepositoryDto(null, new ApiGitHubOwnerDto("123"), null), List.of(new ApiGitHubBranchDto("123", new ApiGitHubCommitDto("123"))));
    repositories.put(new ApiGitHubRepositoryDto("456", new ApiGitHubOwnerDto(null), null), List.of(new ApiGitHubBranchDto("456", new ApiGitHubCommitDto("456"))));
    repositories.put(new ApiGitHubRepositoryDto("789", null, null), List.of());


    List<RepositoryDto> expected = Arrays.asList(
        new RepositoryDto(null, "123", List.of(new BranchDto("123", "123"))),
        new RepositoryDto("456", null, List.of(new BranchDto("456", "456"))),
        new RepositoryDto("789", null, List.of())
    );

    List<RepositoryDto> current = new ArrayList<>(gitHubDtoConverter.mapOfApiGitHubRepositoryDtoAndApiGitHubBranchDtosToListOfRepositoryDto(repositories));

    assertThat(current).isEqualTo(expected);
  }

  @Test
  void apiGitHubBranchDtoToBranchDto() {
    ApiGitHubBranchDto apiGitHubBranchDtoFull = new ApiGitHubBranchDto("123", new ApiGitHubCommitDto("123"));
    ApiGitHubBranchDto apiGitHubBranchDtoWithNullFirst = new ApiGitHubBranchDto(null, new ApiGitHubCommitDto("123"));
    ApiGitHubBranchDto apiGitHubBranchDtoWithNullSecond = new ApiGitHubBranchDto("123", new ApiGitHubCommitDto(null));
    ApiGitHubBranchDto apiGitHubBranchDtoWithNullThird = new ApiGitHubBranchDto("123", null);

    BranchDto branchDtoFirst = new BranchDto("123", "123");
    BranchDto branchDtoSecond = new BranchDto(null, "123");
    BranchDto branchDtoThird = new BranchDto("123", null);
    BranchDto branchDtoFourth = new BranchDto("123", null);

    assertAll(
        () -> assertThat(gitHubDtoConverter.apiGitHubBranchDtoToBranchDto(apiGitHubBranchDtoFull)).isEqualTo(branchDtoFirst),
        () -> assertThat(gitHubDtoConverter.apiGitHubBranchDtoToBranchDto(apiGitHubBranchDtoWithNullFirst)).isEqualTo(branchDtoSecond),
        () -> assertThat(gitHubDtoConverter.apiGitHubBranchDtoToBranchDto(apiGitHubBranchDtoWithNullSecond)).isEqualTo(branchDtoThird),
        () -> assertThat(gitHubDtoConverter.apiGitHubBranchDtoToBranchDto(apiGitHubBranchDtoWithNullThird)).isEqualTo(branchDtoFourth)
    );

  }
}