package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="wishlist")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID wishlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserTable userTable;

    @Column(name = "product_link", nullable = false, columnDefinition = "text")
    private String productLink;

    @Column(name = "item_vector", columnDefinition = "vector()")
    private float[] itemVector;

    @Column(name = "added_at", columnDefinition = "timestamptz default current_timestamp")
    private OffsetDateTime addedAt;

    public UUID getWishlistId() { return wishlistId; }

    public String getProductLink() { return productLink; }

    public float[] getItemVector() { return itemVector; }

    public OffsetDateTime getAddedAt() { return addedAt; }

    public void setUserTable(UserTable userTable) { this.userTable = userTable; }

    public void setProductLink(String productLink) { this.productLink = productLink; }

    public void setItemVector(float[] itemVector) { this.itemVector = itemVector; }
}
