package com.articleplatform.article_platform.article.controller;


import com.articleplatform.article_platform.article.dto.ArticleResponse;
import com.articleplatform.article_platform.article.dto.CreateArticleRequest;
import com.articleplatform.article_platform.article.service.ArticleService;
import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final UserRepository userRepository;

    public ArticleController(
            ArticleService articleService,
            UserRepository userRepository
    ) {
        this.articleService = articleService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getPublishedArticles() {

        return ResponseEntity.ok(
                articleService.getPublishedArticles()
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleResponse> getArticleBySlug(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                articleService.getPublishedArticleBySlug(slug)
        );
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(
            @Valid @RequestBody CreateArticleRequest request,
            Authentication authentication
    ) {

        User author = getAuthenticatedUser(authentication);

        ArticleResponse response = articleService.createArticle(
                request.getTitle(),
                request.getSlug(),
                request.getSummary(),
                request.getContent(),
                author
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{articleId}/publish")
    public ResponseEntity<ArticleResponse> publishArticle(
            @PathVariable Long articleId,
            Authentication authentication
    ) {

        User author = getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                articleService.publishArticle(articleId, author)
        );
    }

    private User getAuthenticatedUser(Authentication authentication) {

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Authenticated user not found"
                        )
                );
    }
}


