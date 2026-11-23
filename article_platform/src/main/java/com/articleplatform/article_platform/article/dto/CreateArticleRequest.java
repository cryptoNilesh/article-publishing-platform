package com.articleplatform.article_platform.article.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateArticleRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Slug is required")
    @Size(max = 250, message = "Slug cannot exceed 250 characters")
    private String slug;

    @Size(max = 500, message = "Summary cannot exceed 500 characters")
    private String summary;

    @NotBlank(message = "Content is required")
    private String content;

    public CreateArticleRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

