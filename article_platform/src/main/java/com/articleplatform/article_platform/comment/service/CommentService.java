package com.articleplatform.article_platform.comment.service;


import com.articleplatform.article_platform.article.entity.Article;
import com.articleplatform.article_platform.article.repository.ArticleRepository;
import com.articleplatform.article_platform.comment.dto.CommentResponse;
import com.articleplatform.article_platform.comment.entity.Comment;
import com.articleplatform.article_platform.comment.mapper.CommentMapper;
import com.articleplatform.article_platform.comment.repository.CommentRepository;
import com.articleplatform.article_platform.exception.NotFoundException;
import com.articleplatform.article_platform.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentService(
            CommentRepository commentRepository,
            ArticleRepository articleRepository
    ) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    public CommentResponse addComment(
            Long articleId,
            String content,
            User author
    ) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new NotFoundException("Article not found"));

        if (!"PUBLISHED".equals(article.getStatus().name())) {
            throw new IllegalArgumentException(
                    "Comments can only be added to published articles"
            );
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticle(article);
        comment.setAuthor(author);
        comment.setParentComment(null);

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toResponse(savedComment);
    }

    public CommentResponse addReply(
            Long articleId,
            Long parentCommentId,
            String content,
            User author
    ) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new NotFoundException("Article not found"));

        if (!"PUBLISHED".equals(article.getStatus().name())) {
            throw new IllegalArgumentException(
                    "Replies can only be added to published articles"
            );
        }

        Comment parentComment = commentRepository
                .findByIdAndArticleId(parentCommentId, articleId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Parent comment not found in this article"
                        )
                );

        Comment reply = new Comment();
        reply.setContent(content);
        reply.setArticle(article);
        reply.setAuthor(author);
        reply.setParentComment(parentComment);

        Comment savedReply = commentRepository.save(reply);

        return CommentMapper.toResponse(savedReply);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getArticleComments(Long articleId) {

        if (!articleRepository.existsById(articleId)) {
            throw new NotFoundException("Article not found");
        }

        return commentRepository
                .findByArticleIdAndParentCommentIsNull(articleId)
                .stream()
                .map(CommentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getReplies(
            Long articleId,
            Long parentCommentId
    ) {

        commentRepository.findByIdAndArticleId(parentCommentId, articleId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Parent comment not found in this article"
                        )
                );

        return commentRepository
                .findByParentCommentId(parentCommentId)
                .stream()
                .map(CommentMapper::toResponse)
                .toList();
    }
}

