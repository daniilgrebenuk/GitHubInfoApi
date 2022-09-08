package com.githubinfo.dto.githubapi;

public record ApiGitHubRepositoryDto(
    String name,
    ApiGitHubOwnerDto owner,
    boolean fork
) {

}
