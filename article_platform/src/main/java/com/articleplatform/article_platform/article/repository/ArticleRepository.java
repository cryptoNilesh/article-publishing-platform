package com.articleplatform.article_platform.article.repository;

import com.articleplatform.article_platform.article.entity.Article;
import com.articleplatform.article_platform.article.enums.ArticleStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    List<Article> findByStatus(ArticleStatus status);


    Optional<Article> findBySlug(String slug);


    boolean existsBySlug(String slug);

    List<Article> findByAuthorId(Long authorId);


    List<Article> findByAuthorIdAndStatus(
            Long authorId,
            ArticleStatus status
    );

    Optional<Article> findBySlugAndStatus(
            String slug,
            ArticleStatus status
    );
}

