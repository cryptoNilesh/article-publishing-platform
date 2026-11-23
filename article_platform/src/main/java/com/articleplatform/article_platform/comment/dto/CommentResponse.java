package com.articleplatform.article_platform.comment.dto;


import java.time.OffsetDateTime;

public class CommentResponse {

    private Long id;
    private String content;
    private Long articleId;
    private Long authorId;
    private String authorUsername;
    private Long parentCommentId;
    private OffsetDateTime createdAt;

    public CommentResponse() {
    }

    public CommentResponse(
            Long id,
            String content,
            Long articleId,
            Long authorId,
            String authorUsername,
            Long parentCommentId,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.content = content;
        this.articleId = articleId;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

