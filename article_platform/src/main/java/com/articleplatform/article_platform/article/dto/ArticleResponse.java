package com.articleplatform.article_platform.article.dto;


import com.articleplatform.article_platform.article.enums.ArticleStatus;

import java.time.OffsetDateTime;

public class ArticleResponse {

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private ArticleStatus status;
    private OffsetDateTime publishedAt;
    private Long authorId;
    private String authorUsername;
    private OffsetDateTime createdAt;

    public ArticleResponse() {
    }

    public ArticleResponse(
            Long id,
            String title,
            String slug,
            String summary,
            String content,
            ArticleStatus status,
            OffsetDateTime publishedAt,
            Long authorId,
            String authorUsername,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.summary = summary;
        this.content = content;
        this.status = status;
        this.publishedAt = publishedAt;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

