package com.outfit.ai.cloth_app.dto;

import java.util.UUID;

public class RecentOutfitDto {
    private UUID id;
    private String name;
    private String imageUrl;
    private String createdDate;

    public RecentOutfitDto(UUID id, String name, String imageUrl, String createdDate) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public String getImageUrl() { return imageUrl; }

    public String getCreatedDate() { return createdDate; }

    public void setId(UUID id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
