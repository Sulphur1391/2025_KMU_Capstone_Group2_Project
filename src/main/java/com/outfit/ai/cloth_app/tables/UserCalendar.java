package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name="user_calendar")
public class UserCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventId;

    @Column(name = "google_event_id", nullable = false, unique = true)
    private String googleEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserTable userTable;

    @Column(name = "start_time", nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime endTime;

    @Column(name = "event_summary", nullable = false, length = 255)
    private String eventSummary;

    @Column(name = "dress_code_tag", length = 255)
    private String dressCodeTag;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "json_data", columnDefinition = "jsonb")
    private String jsonData;

    @Column(name = "is_all_day", columnDefinition = "boolean default false")
    private Boolean isAllDay;

    @Column(name = "created_at", columnDefinition = "timestamptz default current_timestamp")
    private OffsetDateTime createdAt;

    public UUID getEventId() { return eventId; }

    public String getGoogleEventId() { return googleEventId; }

    public UserTable getUserTable() { return userTable; }

    public OffsetDateTime getStartTime() { return startTime; }

    public OffsetDateTime getEndTime() { return endTime; }

    public String getEventSummary() { return eventSummary; }

    public String getDressCodeTag() { return dressCodeTag; }

    public String getLocation() { return location; }

    public String getJsonData() { return jsonData; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public void setGoogleEventId(String googleEventId) { this.googleEventId = googleEventId; }

    public void setUserTable(UserTable userTable) { this.userTable = userTable; }

    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }

    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }

    public void setEventSummary(String eventSummary) { this.eventSummary = eventSummary; }

    public void setDressCodeTag(String dressCodeTag) { this.dressCodeTag = dressCodeTag; }

    public void setLocation(String location) { this.location = location; }

    public void setJsonData(String jsonData) { this.jsonData = jsonData; }

    public void setIsAllDay(Boolean allDay) { isAllDay = allDay; }
}
