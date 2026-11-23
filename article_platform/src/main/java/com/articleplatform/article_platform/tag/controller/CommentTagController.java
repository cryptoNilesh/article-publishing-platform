package com.articleplatform.article_platform.tag.controller;

import com.articleplatform.article_platform.exception.NotFoundException;
import com.articleplatform.article_platform.tag.dto.TagRequest;
import com.articleplatform.article_platform.tag.dto.TagResponse;
import com.articleplatform.article_platform.tag.service.TagService;
import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/{commentId}/tags")
public class CommentTagController {

    private final TagService tagService;
    private final UserRepository userRepository;

    public CommentTagController(
            TagService tagService,
            UserRepository userRepository
    ) {
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<TagResponse> tagUserInComment(
            @PathVariable Long commentId,
            @Valid @RequestBody TagRequest request,
            Authentication authentication
    ) {

        User taggedByUser = getAuthenticatedUser(authentication);

        TagResponse response = tagService.tagUserInComment(
                commentId,
                request.getUsername(),
                taggedByUser
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getCommentTags(
            @PathVariable Long commentId
    ) {

        return ResponseEntity.ok(
                tagService.getCommentTags(commentId)
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
