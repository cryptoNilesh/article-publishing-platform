package com.articleplatform.article_platform.like.service;

import com.articleplatform.article_platform.article.entity.Article;
import com.articleplatform.article_platform.article.repository.ArticleRepository;
import com.articleplatform.article_platform.exception.DuplicateResourceException;
import com.articleplatform.article_platform.exception.NotFoundException;
import com.articleplatform.article_platform.like.entity.ArticleLike;
import com.articleplatform.article_platform.like.repository.ArticleLikeRepository;
import com.articleplatform.article_platform.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;

    public ArticleLikeService(
            ArticleLikeRepository articleLikeRepository,
            ArticleRepository articleRepository
    ) {
        this.articleLikeRepository = articleLikeRepository;
        this.articleRepository = articleRepository;
    }

    public void likeArticle(Long articleId, User user) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new NotFoundException("Article not found"));

        if (!articleLikeRepository.existsByArticleIdAndUserId(
                articleId, user.getId())) {

            ArticleLike articleLike = new ArticleLike();
            articleLike.setArticle(article);
            articleLike.setUser(user);

            articleLikeRepository.save(articleLike);
        } else {
            throw new DuplicateResourceException(
                    "You have already liked this article"
            );
        }
    }

    public void unlikeArticle(Long articleId, User user) {

        articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new NotFoundException("Article not found"));

        ArticleLike articleLike = articleLikeRepository
                .findByArticleIdAndUserId(articleId, user.getId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "You have not liked this article"
                        )
                );

        articleLikeRepository.delete(articleLike);
    }

    @Transactional(readOnly = true)
    public long getLikeCount(Long articleId) {

        if (!articleRepository.existsById(articleId)) {
            throw new NotFoundException("Article not found");
        }

        return articleLikeRepository.countByArticleId(articleId);
    }

    @Transactional(readOnly = true)
    public boolean isArticleLikedByUser(Long articleId, Long userId) {

        if (!articleRepository.existsById(articleId)) {
            throw new NotFoundException("Article not found");
        }

        return articleLikeRepository
                .existsByArticleIdAndUserId(articleId, userId);
    }
}
