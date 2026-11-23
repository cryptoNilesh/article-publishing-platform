package com.articleplatform.article_platform.comment.mapper;


import com.articleplatform.article_platform.comment.dto.CommentResponse;
import com.articleplatform.article_platform.comment.entity.Comment;
import com.articleplatform.article_platform.user.entity.User;

public class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponse toResponse(Comment comment) {

        User author = comment.getAuthor();

        Long parentCommentId = comment.getParentComment() != null
                ? comment.getParentComment().getId()
                : null;

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getArticle().getId(),
                author.getId(),
                author.getUsername(),
                parentCommentId,
                comment.getCreatedAt()
        );
    }
}

