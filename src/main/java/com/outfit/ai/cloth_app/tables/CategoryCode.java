package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

@Entity
@Table(name="category_code")
public class CategoryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryId;

    @Column(name = "category_name", nullable = false, unique = true, length = 255)
    private String categoryName;

    public CategoryCode() {}

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
