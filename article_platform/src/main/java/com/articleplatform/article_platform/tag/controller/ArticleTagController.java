package com.articleplatform.article_platform.tag.controller;

import com.articleplatform.article_platform.tag.dto.TagRequest;
import com.articleplatform.article_platform.tag.dto.TagResponse;
import com.articleplatform.article_platform.tag.service.TagService;
import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import com.articleplatform.article_platform.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/tags")
public class ArticleTagController {

    private final TagService tagService;
    private final UserRepository userRepository;

    public ArticleTagController(
            TagService tagService,
            UserRepository userRepository
    ) {
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<TagResponse> tagUserInArticle(
            @PathVariable Long articleId,
            @Valid @RequestBody TagRequest request,
            Authentication authentication
    ) {

        User taggedByUser = getAuthenticatedUser(authentication);

        TagResponse response = tagService.tagUserInArticle(
                articleId,
                request.getUsername(),
                taggedByUser
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getArticleTags(
            @PathVariable Long articleId
    ) {

        return ResponseEntity.ok(
                tagService.getArticleTags(articleId)
        );
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
                        new NotFoundException("Authenticated user not found")
                );
    }
}

