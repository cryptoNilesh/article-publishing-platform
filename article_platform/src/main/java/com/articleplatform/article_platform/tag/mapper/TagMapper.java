package com.articleplatform.article_platform.tag.mapper;


import com.articleplatform.article_platform.tag.dto.TagResponse;
import com.articleplatform.article_platform.tag.entity.ArticleTag;
import com.articleplatform.article_platform.tag.entity.CommentTag;
import com.articleplatform.article_platform.user.entity.User;

public class TagMapper {

    private TagMapper() {
    }

    public static TagResponse toArticleTagResponse(ArticleTag tag) {

        User taggedUser = tag.getTaggedUser();
        User taggedByUser = tag.getTaggedByUser();

        return new TagResponse(
                tag.getId(),
                taggedUser.getId(),
                taggedUser.getUsername(),
                taggedByUser.getId(),
                taggedByUser.getUsername(),
                tag.getCreatedAt()
        );
    }

    public static TagResponse toCommentTagResponse(CommentTag tag) {

        User taggedUser = tag.getTaggedUser();
        User taggedByUser = tag.getTaggedByUser();

        return new TagResponse(
                tag.getId(),
                taggedUser.getId(),
                taggedUser.getUsername(),
                taggedByUser.getId(),
                taggedByUser.getUsername(),
                tag.getCreatedAt()
        );
    }
}

