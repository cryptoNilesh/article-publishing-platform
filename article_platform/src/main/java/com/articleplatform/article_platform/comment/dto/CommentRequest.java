package com.articleplatform.article_platform.comment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {

    @NotBlank(message = "Comment content is required")
    @Size(max = 5000, message = "Comment cannot exceed 5000 characters")
    private String content;

    public CommentRequest() {
    }

    public CommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

