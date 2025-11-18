package com.outfit.ai.cloth_app.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Embeddable
public class WeatherId implements Serializable {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "weather_id", nullable = false)
    private UUID weatherId;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public WeatherId() {}

    public WeatherId(UUID weatherId, OffsetDateTime createdAt) {
        this.weatherId = weatherId;
        this.createdAt = createdAt;
    }

    public UUID getWeatherId() { return weatherId; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setWeatherId(UUID weatherId) { this.weatherId = weatherId; }

    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherId weatherId1 = (WeatherId) o;
        return weatherId.equals(weatherId1.weatherId) && createdAt.equals(weatherId1.createdAt);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(weatherId, createdAt);
    }
}
