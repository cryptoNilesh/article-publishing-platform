package com.articleplatform.article_platform.tag.repository;


import com.articleplatform.article_platform.tag.entity.CommentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {

    List<CommentTag> findByCommentId(Long commentId);

    boolean existsByCommentIdAndTaggedUserId(Long commentId, Long taggedUserId);

    Optional<CommentTag> findByCommentIdAndTaggedUserId(Long commentId, Long taggedUserId);

    List<CommentTag> findByTaggedByUserId(Long userId);

    List<CommentTag> findByTaggedUserId(Long userId);

    void deleteByCommentId(Long commentId);
}

