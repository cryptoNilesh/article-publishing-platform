package com.articleplatform.article_platform.comment.repository;


import com.articleplatform.article_platform.comment.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleId(Long articleId);


    List<Comment> findByArticleIdAndParentCommentIsNull(Long articleId);


    List<Comment> findByParentCommentId(Long parentCommentId);


    boolean existsById(Long id);


    Optional<Comment> findByIdAndArticleId(Long id, Long articleId);


    List<Comment> findByAuthorId(Long authorId);
}

