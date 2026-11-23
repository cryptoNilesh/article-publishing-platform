package com.articleplatform.article_platform.tag.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    public TagRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

