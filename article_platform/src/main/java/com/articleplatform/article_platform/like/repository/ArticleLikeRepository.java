package com.articleplatform.article_platform.like.repository;


import com.articleplatform.article_platform.like.entity.ArticleLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository
        extends JpaRepository<ArticleLike, Long> {

    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

    long countByArticleId(Long articleId);

    Optional<ArticleLike> findByArticleIdAndUserId(
            Long articleId,
            Long userId
    );

    long countByUserId(Long userId);

    void deleteByArticleIdAndUserId(Long articleId, Long userId);
}

