package com.githubinfo.converter;

import com.githubinfo.dto.BranchDto;
import com.githubinfo.dto.RepositoryDto;
import com.githubinfo.dto.githubapi.ApiGitHubBranchDto;
import com.githubinfo.dto.githubapi.ApiGitHubCommitDto;
import com.githubinfo.dto.githubapi.ApiGitHubOwnerDto;
import com.githubinfo.dto.githubapi.ApiGitHubRepositoryDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GitHubDtoConverter {

  public List<RepositoryDto> mapOfApiGitHubRepositoryDtoAndApiGitHubBranchDtosToListOfRepositoryDto(Map<ApiGitHubRepositoryDto, List<ApiGitHubBranchDto>> repositories) {
    return repositories.entrySet()
        .stream()
        .map(e -> {
          String repositoryName = e.getKey().name();
          String owner = Optional.of(e.getKey()).map(ApiGitHubRepositoryDto::owner).map(ApiGitHubOwnerDto::login).orElse(null);
          List<BranchDto> branches = e.getValue()
              .stream()
              .map(this::apiGitHubBranchDtoToBranchDto)
              .toList();
          return new RepositoryDto(repositoryName, owner, branches);
        })
        .toList();
  }

  public BranchDto apiGitHubBranchDtoToBranchDto(ApiGitHubBranchDto apiGitHubBranchDto) {
    return new BranchDto(
        apiGitHubBranchDto.name(),
        Optional.of(apiGitHubBranchDto).map(ApiGitHubBranchDto::commit).map(ApiGitHubCommitDto::sha).orElse(null)
    );
  }
}
