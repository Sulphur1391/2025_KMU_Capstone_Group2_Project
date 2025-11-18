package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

@Entity
@Table(name="material_code")
public class MaterialCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int materialId;

    @Column(name = "material_name", nullable = false, unique = true, length = 255)
    private String materialName;

    public MaterialCode() {}

    public String getMaterialName() { return materialName; }

    public int getMaterialId() { return materialId; }

    public void setMaterialName(String materialName) { this.materialName = materialName; }
}
