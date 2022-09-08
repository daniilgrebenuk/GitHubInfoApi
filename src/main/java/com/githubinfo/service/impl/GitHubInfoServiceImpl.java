package com.githubinfo.service.impl;

import com.githubinfo.converter.GitHubDtoConverter;
import com.githubinfo.dto.RepositoryDto;
import com.githubinfo.dto.githubapi.ApiGitHubBranchDto;
import com.githubinfo.dto.githubapi.ApiGitHubRepositoryDto;
import com.githubinfo.service.GitHubInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitHubInfoServiceImpl implements GitHubInfoService {

  private final RestTemplate restTemplate;
  private final GitHubDtoConverter gitHubDtoConverter;

  @Value("${default-github-api-url}")
  private String defaultGitHubApiUrl;

  @Override
  public List<RepositoryDto> findGitHubInfoByUsername(String username) {
    Map<ApiGitHubRepositoryDto, List<ApiGitHubBranchDto>> responseMap = findAllRepositoriesByUsername(username)
        .stream()
        .filter(apiGitHubRepositoryDto -> !apiGitHubRepositoryDto.fork())
        .collect(Collectors.toMap(
            Function.identity(),
            apiGitHubRepositoryDto -> findAllBranchByUsernameAndRepositoryName(apiGitHubRepositoryDto.owner().login(), apiGitHubRepositoryDto.name())
        ));

    return gitHubDtoConverter.mapOfApiGitHubRepositoryDtoAndApiGitHubBranchDtosToListOfRepositoryDto(responseMap);
  }

  private List<ApiGitHubRepositoryDto> findAllRepositoriesByUsername(String username) {
    return findAllPageableElementsFromApi(
        String.format("%s/users/%s/repos", defaultGitHubApiUrl, username),
        ApiGitHubRepositoryDto[].class,
        100
    );
  }

  private List<ApiGitHubBranchDto> findAllBranchByUsernameAndRepositoryName(String username, String repositoryName) {
    return findAllPageableElementsFromApi(
        String.format("%s/repos/%s/%s/branches", defaultGitHubApiUrl, username, repositoryName),
        ApiGitHubBranchDto[].class,
        100
    );
  }

  private <T> List<T> findAllPageableElementsFromApi(String url, Class<T[]> tClass, int numberOfElementsOnPage) {
    int page = 1;
    List<T> resultList = new ArrayList<>();
    while (resultList.size() % numberOfElementsOnPage == 0) {
      ResponseEntity<T[]> responseBranches = restTemplate.getForEntity(
          UriComponentsBuilder.fromHttpUrl(url)
              .queryParam("per_page", numberOfElementsOnPage)
              .queryParam("page", page++)
              .build().toUriString(),
          tClass
      );
      T[] body = responseBranches.getBody();
      if (body == null || body.length == 0) {
        break;
      }
      resultList.addAll(Arrays.asList(Objects.requireNonNull(responseBranches.getBody())));
    }
    return resultList;
  }
}
