package com.articleplatform.article_platform.tag.repository;


import com.articleplatform.article_platform.tag.entity.ArticleTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleTagRepository
        extends JpaRepository<ArticleTag, Long> {

    List<ArticleTag> findByArticleId(Long articleId);

    boolean existsByArticleIdAndTaggedUserId(
            Long articleId,
            Long taggedUserId
    );

    Optional<ArticleTag> findByArticleIdAndTaggedUserId(
            Long articleId,
            Long taggedUserId
    );

    List<ArticleTag> findByTaggedByUserId(Long userId);


    List<ArticleTag> findByTaggedUserId(Long userId);


    void deleteByArticleId(Long articleId);
}

