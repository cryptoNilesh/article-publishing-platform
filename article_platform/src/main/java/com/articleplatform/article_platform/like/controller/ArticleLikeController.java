package com.articleplatform.article_platform.like.controller;

import com.articleplatform.article_platform.like.dto.LikeResponse;
import com.articleplatform.article_platform.like.service.ArticleLikeService;
import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles/{articleId}/likes")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;
    private final UserRepository userRepository;

    public ArticleLikeController(
            ArticleLikeService articleLikeService,
            UserRepository userRepository
    ) {
        this.articleLikeService = articleLikeService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> likeArticle(
            @PathVariable Long articleId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        articleLikeService.likeArticle(articleId, user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Article liked successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> unlikeArticle(
            @PathVariable Long articleId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        articleLikeService.unlikeArticle(articleId, user);

        return ResponseEntity.ok("Article unliked successfully");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Long articleId
    ) {

        return ResponseEntity.ok(
                articleLikeService.getLikeCount(articleId)
        );
    }

    @GetMapping("/status")
    public ResponseEntity<LikeResponse> getLikeStatus(
            @PathVariable Long articleId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        long likeCount = articleLikeService.getLikeCount(articleId);

        boolean likedByCurrentUser =
                articleLikeService.isArticleLikedByUser(
                        articleId,
                        user.getId()
                );

        LikeResponse response = new LikeResponse(
                articleId,
                likeCount,
                likedByCurrentUser
        );

        return ResponseEntity.ok(response);
    }

    private User getAuthenticatedUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.authentication
                    .AuthenticationCredentialsNotFoundException(
                    "Authentication is required"
            );
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new com.articleplatform.article_platform.exception
                                .NotFoundException(
                                "Authenticated user not found"
                        )
                );
    }
}

