package com.articleplatform.article_platform.tag.dto;


import java.time.OffsetDateTime;

public class TagResponse {

    private Long id;
    private Long taggedUserId;
    private String taggedUsername;
    private Long taggedByUserId;
    private String taggedByUsername;
    private OffsetDateTime createdAt;

    public TagResponse() {
    }

    public TagResponse(
            Long id,
            Long taggedUserId,
            String taggedUsername,
            Long taggedByUserId,
            String taggedByUsername,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.taggedUserId = taggedUserId;
        this.taggedUsername = taggedUsername;
        this.taggedByUserId = taggedByUserId;
        this.taggedByUsername = taggedByUsername;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTaggedUserId() {
        return taggedUserId;
    }

    public String getTaggedUsername() {
        return taggedUsername;
    }

    public Long getTaggedByUserId() {
        return taggedByUserId;
    }

    public String getTaggedByUsername() {
        return taggedByUsername;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
