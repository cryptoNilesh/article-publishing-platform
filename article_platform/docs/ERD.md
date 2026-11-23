
# Article Publishing Platform - Entity Relationship Diagram

## Database: PostgreSQL

The application uses six main entities: users, articles, comments, article_likes, article_tags and comment_tags.

```mermaid
erDiagram
    USERS ||--o{ ARTICLES : writes
    USERS ||--o{ COMMENTS : creates
    USERS ||--o{ ARTICLE_LIKES : gives
    ARTICLES ||--o{ ARTICLE_LIKES : receives
    ARTICLES ||--o{ COMMENTS : contains
    COMMENTS ||--o{ COMMENTS : replies_to
    ARTICLES ||--o{ ARTICLE_TAGS : has
    USERS ||--o{ ARTICLE_TAGS : tagged_user
    USERS ||--o{ ARTICLE_TAGS : tagged_by
    COMMENTS ||--o{ COMMENT_TAGS : has
    USERS ||--o{ COMMENT_TAGS : tagged_user
    USERS ||--o{ COMMENT_TAGS : tagged_by

    USERS {
        bigint id PK
        varchar username UK
        varchar email UK
        varchar password_hash
        varchar display_name
        varchar role
        boolean enabled
        timestamp created_at
        timestamp updated_at
    }

    ARTICLES {
        bigint id PK
        varchar title
        varchar slug UK
        varchar summary
        text content
        varchar status
        timestamp published_at
        bigint author_id FK
        timestamp created_at
        timestamp updated_at
    }

    COMMENTS {
        bigint id PK
        text content
        bigint article_id FK
        bigint author_id FK
        bigint parent_comment_id FK
        timestamp created_at
        timestamp updated_at
    }

    ARTICLE_LIKES {
        bigint id PK
        bigint article_id FK
        bigint user_id FK
        timestamp created_at
    }

    ARTICLE_TAGS {
        bigint id PK
        bigint article_id FK
        bigint tagged_user_id FK
        bigint tagged_by_user_id FK
        timestamp created_at
    }

    COMMENT_TAGS {
        bigint id PK
        bigint comment_id FK
        bigint tagged_user_id FK
        bigint tagged_by_user_id FK
        timestamp created_at
    }
```

## Relationships and Constraints

- One user can create multiple articles.
- One article can contain multiple comments.
- Comments support nested replies through `parent_comment_id`.
- One user can like an article only once.
- Users can be tagged in articles and comments.
- `tagged_user_id` identifies the mentioned user.
- `tagged_by_user_id` identifies the user who created the tag.
- Article slugs, usernames and email addresses are unique.
- Like and tag relationships have unique constraints to prevent duplicate records.
