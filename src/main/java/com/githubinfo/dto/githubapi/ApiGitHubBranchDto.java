package com.githubinfo.dto.githubapi;

public record ApiGitHubBranchDto(
    String name,
    ApiGitHubCommitDto commit
) {

}
