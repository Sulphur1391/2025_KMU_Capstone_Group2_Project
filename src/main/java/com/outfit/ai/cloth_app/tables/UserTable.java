package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="user_table")
public class UserTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @OneToMany(mappedBy = "userTable", cascade = CascadeType.ALL)
    private List<UserCalendar> userCalendarList = new ArrayList<>();

    @OneToMany(mappedBy = "userTable", cascade = CascadeType.ALL)
    private List<Wishlist> wishlistList = new ArrayList<>();

    @OneToMany(mappedBy = "userTable", cascade = CascadeType.ALL)
    private List<CommunityInteractions> communityInteractionsList = new ArrayList<>();

    @OneToMany(mappedBy = "userTable", cascade = CascadeType.ALL)
    private List<OutfitCombination> outfitCombinationList = new ArrayList<>();

    @OneToMany(mappedBy = "userTable", cascade = CascadeType.ALL)
    private List<ClothesTable> clothesTableList = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<MessageTable> sender;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<MessageTable> receiver;

    @Column(name = "username", nullable = false, unique = true, length = 255)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "profile_image_url", columnDefinition = "text")
    private String profileImageUrl;

    @Column(name = "region", length = 255)
    private String region;

    @Column(name = "created_at", columnDefinition = "timestamptz default current_timestamp")
    private OffsetDateTime createdAt;

    public UUID getUserId() { return userId; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getPasswordHash() { return passwordHash; }

    public String getProfileImageUrl() { return profileImageUrl; }

    public String getRegion() { return region; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setUserCalendarList(List<UserCalendar> userCalendarList) { this.userCalendarList = userCalendarList; }

    public void setWishlistList(List<Wishlist> wishlistList) { this.wishlistList = wishlistList; }

    public void setCommunityInteractionsList(List<CommunityInteractions> communityInteractionsList) { this.communityInteractionsList = communityInteractionsList; }

    public void setOutfitCombinationList(List<OutfitCombination> outfitCombinationList) { this.outfitCombinationList = outfitCombinationList; }

    public void setClothesTableList(List<ClothesTable> clothesTableList) { this.clothesTableList = clothesTableList; }

    public void setUsername(String username) { this.username = username; }

    public void setEmail(String email) { this.email = email; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public void setRegion(String region) { this.region = region; }
}
