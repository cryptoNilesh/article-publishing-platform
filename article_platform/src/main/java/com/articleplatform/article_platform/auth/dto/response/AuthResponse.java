package com.articleplatform.article_platform.auth.dto.response;


public class AuthResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String username;
    private String email;
    private String displayName;

    public AuthResponse() {
    }

    public AuthResponse(
            String token,
            String tokenType,
            Long userId,
            String username,
            String email,
            String displayName
    ) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
