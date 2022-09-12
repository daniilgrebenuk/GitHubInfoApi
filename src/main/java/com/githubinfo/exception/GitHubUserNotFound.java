package com.githubinfo.exception;

import com.githubinfo.constant.GitHubInfoErrorConstants;
import lombok.Getter;

@Getter
public class GitHubUserNotFound extends RuntimeException {

  private String username;

  public GitHubUserNotFound() {
  }

  public GitHubUserNotFound(String username) {
    super(String.format(GitHubInfoErrorConstants.USER_NOT_FOUND, username));
    this.username = username;
  }

  public GitHubUserNotFound(String username, Throwable cause) {
    super(String.format(GitHubInfoErrorConstants.USER_NOT_FOUND, username), cause);
    this.username = username;
  }
}
