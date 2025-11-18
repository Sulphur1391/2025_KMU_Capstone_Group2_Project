package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="clothes_table")
public class ClothesTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID clothId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserTable userTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryCode categoryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private ColorCode colorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private MaterialCode materialCode;

    @Column(name = "name", nullable = false, length = 255)
    private String clothName;

    @Column(name = "image_url", nullable = false, columnDefinition = "text")
    private String imageUrl;

    @Column(name = "style_vector", columnDefinition = "vector()")
    private float[] styleVector;

    @Column(name = "created_at", columnDefinition = "timestamptz default current_timestamp")
    private OffsetDateTime createdAt;

    public ClothesTable() {}
    public UserTable getUserTable() { return userTable; }
    public CategoryCode getCategoryCode() { return categoryCode; }
    public ColorCode getColorCode() { return colorCode; }
    public MaterialCode getMaterialCode() { return materialCode; }
    public UUID getClothId() { return clothId; }
    public String getClothName() { return clothName; }
    public String getImageUrl() { return imageUrl; }
    public float[] getStyleVector() { return styleVector; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setClothName(String clothName) { this.clothName = clothName; }
    public void setCategoryCode(CategoryCode categoryCode) { this.categoryCode = categoryCode; }
    public void setColorCode(ColorCode colorCode) { this.colorCode = colorCode; }
    public void setMaterialCode(MaterialCode materialCode) { this.materialCode = materialCode; }
    public void setUserTable(UserTable userTable) { this.userTable = userTable; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setStyleVector(float[] styleVector) { this.styleVector = styleVector; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
