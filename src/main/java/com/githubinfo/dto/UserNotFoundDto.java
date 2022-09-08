package com.githubinfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserNotFoundDto(
    int status,
    @JsonProperty("Message") String message
) {

}
