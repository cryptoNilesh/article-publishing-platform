package com.articleplatform.article_platform.article.mapper;



import com.articleplatform.article_platform.article.dto.ArticleResponse;
import com.articleplatform.article_platform.article.entity.Article;
import com.articleplatform.article_platform.user.entity.User;

public class ArticleMapper {

    private ArticleMapper() {
    }

    public static ArticleResponse toResponse(Article article) {

        User author = article.getAuthor();

        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getSummary(),
                article.getContent(),
                article.getStatus(),
                article.getPublishedAt(),
                author.getId(),
                author.getUsername(),
                article.getCreatedAt()
        );
    }
}

