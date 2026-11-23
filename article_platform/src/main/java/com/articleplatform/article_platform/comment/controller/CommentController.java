
package com.articleplatform.article_platform.comment.controller;

import com.articleplatform.article_platform.comment.dto.CommentRequest;
import com.articleplatform.article_platform.comment.dto.CommentResponse;
import com.articleplatform.article_platform.comment.service.CommentService;
import com.articleplatform.article_platform.exception.NotFoundException;
import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    public CommentController(
            CommentService commentService,
            UserRepository userRepository
    ) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getArticleComments(
            @PathVariable Long articleId
    ) {

        List<CommentResponse> comments =
                commentService.getArticleComments(articleId);

        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {

        User author = getAuthenticatedUser(authentication);

        CommentResponse response = commentService.addComment(
                articleId,
                request.getContent(),
                author
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{parentCommentId}/replies")
    public ResponseEntity<CommentResponse> addReply(
            @PathVariable Long articleId,
            @PathVariable Long parentCommentId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {

        User author = getAuthenticatedUser(authentication);

        CommentResponse response = commentService.addReply(
                articleId,
                parentCommentId,
                request.getContent(),
                author
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{parentCommentId}/replies")
    public ResponseEntity<List<CommentResponse>> getReplies(
            @PathVariable Long articleId,
            @PathVariable Long parentCommentId
    ) {

        List<CommentResponse> replies =
                commentService.getReplies(articleId, parentCommentId);

        return ResponseEntity.ok(replies);
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
