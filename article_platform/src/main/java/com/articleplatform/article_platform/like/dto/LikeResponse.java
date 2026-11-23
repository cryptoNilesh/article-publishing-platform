package com.articleplatform.article_platform.like.dto;

public class LikeResponse {

    private Long articleId;
    private long likeCount;
    private boolean likedByCurrentUser;

    public LikeResponse() {
    }

    public LikeResponse(
            Long articleId,
            long likeCount,
            boolean likedByCurrentUser
    ) {
        this.articleId = articleId;
        this.likeCount = likeCount;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public Long getArticleId() {
        return articleId;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }
}

