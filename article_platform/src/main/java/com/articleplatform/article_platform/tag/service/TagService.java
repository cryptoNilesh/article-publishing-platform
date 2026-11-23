package com.articleplatform.article_platform.tag.service;

import com.articleplatform.article_platform.article.entity.Article;
import com.articleplatform.article_platform.article.repository.ArticleRepository;
import com.articleplatform.article_platform.comment.entity.Comment;
import com.articleplatform.article_platform.comment.repository.CommentRepository;
import com.articleplatform.article_platform.exception.DuplicateResourceException;
import com.articleplatform.article_platform.exception.NotFoundException;
import com.articleplatform.article_platform.tag.dto.TagResponse;
import com.articleplatform.article_platform.tag.entity.ArticleTag;
import com.articleplatform.article_platform.tag.entity.CommentTag;
import com.articleplatform.article_platform.tag.mapper.TagMapper;
import com.articleplatform.article_platform.tag.repository.ArticleTagRepository;
import com.articleplatform.article_platform.tag.repository.CommentTagRepository;
import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagService {

    private final ArticleTagRepository articleTagRepository;
    private final CommentTagRepository commentTagRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public TagService(
            ArticleTagRepository articleTagRepository,
            CommentTagRepository commentTagRepository,
            ArticleRepository articleRepository,
            CommentRepository commentRepository,
            UserRepository userRepository
    ) {
        this.articleTagRepository = articleTagRepository;
        this.commentTagRepository = commentTagRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public TagResponse tagUserInArticle(
            Long articleId,
            String username,
            User taggedByUser
    ) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new NotFoundException("Article not found"));

        User taggedUser = findTaggedUser(username);

        validateTag(taggedUser, taggedByUser);

        if (articleTagRepository.existsByArticleIdAndTaggedUserId(
                articleId, taggedUser.getId())) {

            throw new DuplicateResourceException(
                    "User is already tagged in this article"
            );
        }

        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticle(article);
        articleTag.setTaggedUser(taggedUser);
        articleTag.setTaggedByUser(taggedByUser);

        ArticleTag savedTag = articleTagRepository.save(articleTag);

        return TagMapper.toArticleTagResponse(savedTag);
    }

    public TagResponse tagUserInComment(
            Long commentId,
            String username,
            User taggedByUser
    ) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException("Comment not found"));

        User taggedUser = findTaggedUser(username);

        validateTag(taggedUser, taggedByUser);

        if (commentTagRepository.existsByCommentIdAndTaggedUserId(
                commentId, taggedUser.getId())) {

            throw new DuplicateResourceException(
                    "User is already tagged in this comment"
            );
        }

        CommentTag commentTag = new CommentTag();
        commentTag.setComment(comment);
        commentTag.setTaggedUser(taggedUser);
        commentTag.setTaggedByUser(taggedByUser);

        CommentTag savedTag = commentTagRepository.save(commentTag);

        return TagMapper.toCommentTagResponse(savedTag);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getArticleTags(Long articleId) {

        if (!articleRepository.existsById(articleId)) {
            throw new NotFoundException("Article not found");
        }

        return articleTagRepository.findByArticleId(articleId)
                .stream()
                .map(TagMapper::toArticleTagResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getCommentTags(Long commentId) {

        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment not found");
        }

        return commentTagRepository.findByCommentId(commentId)
                .stream()
                .map(TagMapper::toCommentTagResponse)
                .toList();
    }

    private User findTaggedUser(String username) {

        return userRepository.findByUsername(
                        username.trim().toLowerCase()
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Tagged user not found: " + username
                        )
                );
    }

    private void validateTag(User taggedUser, User taggedByUser) {

        if (taggedUser.getId().equals(taggedByUser.getId())) {
            throw new IllegalArgumentException(
                    "You cannot tag yourself"
            );
        }
    }
}

