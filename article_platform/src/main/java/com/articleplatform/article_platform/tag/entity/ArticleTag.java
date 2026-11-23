package com.articleplatform.article_platform.tag.entity;

import com.articleplatform.article_platform.article.entity.Article;
import com.articleplatform.article_platform.user.entity.User;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "article_tags",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_article_tagged_user",
                        columnNames = {"article_id", "tagged_user_id"}
                )
        }
)
public class ArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kis article mein user ko tag kiya gaya
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    // Kaun sa user tag hua
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tagged_user_id", nullable = false)
    private User taggedUser;

    // Kis user ne tag kiya
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tagged_by_user_id", nullable = false)
    private User taggedByUser;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    public ArticleTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getTaggedUser() {
        return taggedUser;
    }

    public void setTaggedUser(User taggedUser) {
        this.taggedUser = taggedUser;
    }

    public User getTaggedByUser() {
        return taggedByUser;
    }

    public void setTaggedByUser(User taggedByUser) {
        this.taggedByUser = taggedByUser;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

