package com.githubinfo.dto.githubapi;

public record GitHubRepositoryDto(
    String name,
    GitHubOwnerDto owner
) {

}
