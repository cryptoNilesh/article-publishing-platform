package com.articleplatform.article_platform.article.service;


import com.articleplatform.article_platform.article.dto.ArticleResponse;
import com.articleplatform.article_platform.article.entity.Article;
import com.articleplatform.article_platform.article.enums.ArticleStatus;
import com.articleplatform.article_platform.article.mapper.ArticleMapper;
import com.articleplatform.article_platform.article.repository.ArticleRepository;
import com.articleplatform.article_platform.exception.DuplicateResourceException;
import com.articleplatform.article_platform.exception.ForbiddenException;
import com.articleplatform.article_platform.exception.NotFoundException;
import com.articleplatform.article_platform.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleResponse createArticle(
            String title,
            String slug,
            String summary,
            String content,
            User author
    ) {

        if (articleRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException(
                    "Article slug already exists"
            );
        }

        Article article = new Article();

        article.setTitle(title);
        article.setSlug(slug);
        article.setSummary(summary);
        article.setContent(content);
        article.setStatus(ArticleStatus.DRAFT);
        article.setAuthor(author);

        Article savedArticle = articleRepository.save(article);

        return ArticleMapper.toResponse(savedArticle);
    }

    public ArticleResponse publishArticle(
            Long articleId,
            User author
    ) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new NotFoundException("Article not found"));

        if (!article.getAuthor().getId().equals(author.getId())) {
            throw new ForbiddenException(
                    "You are not allowed to publish this article"
            );
        }

        article.setStatus(ArticleStatus.PUBLISHED);
        article.setPublishedAt(OffsetDateTime.now());

        Article updatedArticle = articleRepository.save(article);

        return ArticleMapper.toResponse(updatedArticle);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getPublishedArticles() {

        return articleRepository
                .findByStatus(ArticleStatus.PUBLISHED)
                .stream()
                .map(ArticleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleResponse getPublishedArticleBySlug(String slug) {

        Article article = articleRepository
                .findBySlugAndStatus(slug, ArticleStatus.PUBLISHED)
                .orElseThrow(() ->
                        new NotFoundException("Published article not found"));

        return ArticleMapper.toResponse(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticlesByAuthor(Long authorId) {

        return articleRepository.findByAuthorId(authorId)
                .stream()
                .map(ArticleMapper::toResponse)
                .toList();
    }
}

