package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="community_interactions")
public class CommunityInteractions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID interactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outfit_id")
    private OutfitCombination outfitCombination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserTable userTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paren_comment_id")
    private CommunityInteractions parent;

    @OneToMany(mappedBy = "interactionId", cascade = CascadeType.ALL)
    private List<CommunityInteractions> children = new ArrayList<>();

    @Column(name = "interaction_type", nullable = false, unique = true, length = 255)
    private String interactionType;

    @Column(name = "content", length = 255)
    private String content;

    @Column(name = "created_at", columnDefinition = "timestamptz default current_timestamp")
    private OffsetDateTime createdAt;

    public UUID getInteractionId() { return interactionId; }
    public void setInteractionId(UUID interactionId) { this.interactionId = interactionId; }

    public OutfitCombination getOutfitCombination() { return outfitCombination; }
    public void setOutfitCombination(OutfitCombination outfitCombination) { this.outfitCombination = outfitCombination; }

    public UserTable getUserTable() { return userTable; }
    public void setUserTable(UserTable userTable) { this.userTable = userTable; }

    public CommunityInteractions getParentId() { return parent; }
    public void setParentId(CommunityInteractions parent) { this.parent = parent; }

    public List<CommunityInteractions> getChildrenId() { return children; }
    public void setChildrenId(List<CommunityInteractions> children) { this.children = children; }

    public String getInteractionType() { return interactionType; }
    public void setInteractionType(String interactionType) { this.interactionType = interactionType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
