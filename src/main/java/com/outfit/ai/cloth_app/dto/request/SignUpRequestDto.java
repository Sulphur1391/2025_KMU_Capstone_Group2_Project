package com.outfit.ai.cloth_app.dto.request;

public class SignUpRequestDto {
    private String email;
    private String password;
    private String username;
    private String region;

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getUsername() { return username; }

    public String getRegion() { return region; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setUsername(String username) { this.username = username; }

    public void setRegion(String region) { this.region = region; }
}
