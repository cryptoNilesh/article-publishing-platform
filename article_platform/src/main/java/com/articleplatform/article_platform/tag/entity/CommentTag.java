package com.articleplatform.article_platform.tag.entity;


import com.articleplatform.article_platform.comment.entity.Comment;
import com.articleplatform.article_platform.user.entity.User;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "comment_tags",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_comment_tagged_user",
                        columnNames = {"comment_id", "tagged_user_id"}
                )
        }
)
public class CommentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kis comment ya reply mein tag kiya gaya
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

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

    public CommentTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
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

