package com.githubinfo.service.impl;

import com.githubinfo.dto.BranchDto;
import com.githubinfo.dto.RepositoryDto;
import com.githubinfo.dto.githubapi.GitHubRepositoryDto;
import com.githubinfo.service.GitHubInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubInfoServiceImpl implements GitHubInfoService {

  private final RestTemplate restTemplate;

  @Override
  public List<RepositoryDto> findGitHubInfoByUsername(String username) {
    ResponseEntity<GitHubRepositoryDto[]> response = restTemplate.getForEntity(String.format("https://api.github.com/users/%s/repos", username), GitHubRepositoryDto[].class);


    return null;
  }

  private List<BranchDto> findAllBranchByUsernameAndRepositoryName() {
    return null;
  }
}
