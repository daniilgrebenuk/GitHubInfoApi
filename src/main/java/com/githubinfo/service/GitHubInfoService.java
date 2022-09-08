package com.githubinfo.service;


import com.githubinfo.dto.RepositoryDto;

import java.util.List;

public interface GitHubInfoService {

  List<RepositoryDto> findGitHubInfoByUsername(String username);

}
