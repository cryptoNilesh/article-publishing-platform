
# Sequence Diagram - Comment Reply with User Tagging

This diagram describes how an authenticated user replies to an existing comment and tags another registered user.

```mermaid
sequenceDiagram
    actor User as Logged-in User
    participant UI as Angular Frontend
    participant Auth as JWT Security
    participant CC as Comment Controller
    participant CS as Comment Service
    participant DB as PostgreSQL
    participant TC as Tag Controller
    participant TS as Tag Service

    User->>UI: Enter reply with @username
    User->>UI: Submit reply
    UI->>Auth: Send POST request with JWT
    Auth->>Auth: Validate JWT

    alt Invalid or missing token
        Auth-->>UI: 401 Unauthorized
        UI-->>User: Show login message
    else Valid token
        Auth->>CC: Forward authenticated request
        CC->>CS: Create reply
        CS->>DB: Verify parent comment and article
        DB-->>CS: Parent comment found
        CS->>DB: Insert reply with parent_comment_id
        DB-->>CS: Reply created
        CS-->>CC: Return reply response
        CC-->>UI: 201 Created

        UI->>UI: Extract @username mentions
        UI->>TC: POST /api/comments/{commentId}/tags
        TC->>TS: Create comment tag
        TS->>DB: Find tagged user
        DB-->>TS: User found
        TS->>DB: Insert comment_tags record
        DB-->>TS: Tag saved
        TS-->>TC: Return tag response
        TC-->>UI: Tag created
        UI-->>User: Display reply and tagged username
    end
```

## Flow Summary

1. User submits a reply containing an @username mention.
2. JWT authentication validates the request.
3. Comment service verifies the parent comment and article.
4. Reply is saved with its parent comment reference.
5. Angular extracts mentioned usernames.
6. Tag API creates the corresponding comment tag.
7. The frontend displays the saved reply.
