package com.outfit.ai.cloth_app.dto.response;

import java.util.UUID;

<<<<<<< HEAD
// 토큰 응답 DTO
=======
>>>>>>> origin/main
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private UUID userId;
    private String username;

    public String getAccessToken() { return accessToken; }
<<<<<<< HEAD
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
=======

    public String getRefreshToken() { return refreshToken; }

    public UUID getUserId() { return userId; }

    public String getUsername() { return username; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public void setUserId(UUID userId) { this.userId = userId; }

    public void setUsername(String username) { this.username = username; }
}
>>>>>>> origin/main
