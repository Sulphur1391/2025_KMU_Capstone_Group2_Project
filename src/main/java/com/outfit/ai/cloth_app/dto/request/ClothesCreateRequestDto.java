package com.outfit.ai.cloth_app.dto.request;

public class ClothesCreateRequestDto {
    private String name;
    private String categoryName;
    private String materialName;
    private String colorName;

    public String getName() { return name; }
    public String getCategoryName() { return categoryName; }
    public String getMaterialName() { return materialName; }
    public String getColorName() { return colorName; }

    public void setName(String name) { this.name = name; }
    public void setCategoryCode(String categoryName) { this.categoryName = categoryName; }
    public void setMaterialCode(String materialName) { this.materialName = materialName; }
    public void setColorCode(String colorName) { this.colorName = colorName; }
}