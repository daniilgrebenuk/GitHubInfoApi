package com.githubinfo.controller;

import com.githubinfo.dto.RepositoryDto;
import com.githubinfo.service.GitHubInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/github")
public class GitHubInfoController {

  private final GitHubInfoService gitHubInfoService;

  @GetMapping("/user")
  public ResponseEntity<List<RepositoryDto>> getUserInfo(@RequestParam String username) {
    return ResponseEntity.ok(gitHubInfoService.findGitHubInfoByUsername(username));
  }
}
