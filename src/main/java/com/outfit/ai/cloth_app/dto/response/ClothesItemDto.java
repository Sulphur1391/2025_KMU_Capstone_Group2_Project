package com.outfit.ai.cloth_app.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class ClothesItemDto {
    private UUID clothID;
    private String name;
    private String imageUrl;
    private String categoryName;
    private String colorName;
    private String materialName;
    private float[] styleVector;
    private OffsetDateTime createdAt;

    public UUID getClothID() { return clothID; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getCategoryName() { return categoryName; }
    public String getColorName() { return colorName; }
    public String getMaterialName() { return materialName; }
    public float[] getStyleVector() { return styleVector; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setClothID(UUID clothID) { this.clothID = clothID; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setColorName(String colorName) { this.colorName = colorName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public void setStyleVector(float[] styleVector) { this.styleVector = styleVector; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}