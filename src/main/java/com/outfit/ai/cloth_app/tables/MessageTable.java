package com.outfit.ai.cloth_app.tables;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name="message_table")
public class MessageTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserTable sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserTable receiver;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(name = "send_at", columnDefinition = "timestamptz default current_timestamp")
    private OffsetDateTime sentAt;

    @Column(name = "is_read")
    private boolean isRead = false;

    public MessageTable() {}

    public MessageTable(UserTable sender, UserTable receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sentAt = OffsetDateTime.now(); // 생성 시각을 여기서 설정
        this.isRead = false;
    }

    public long getMessageId() { return messageId; }
    public UserTable getSender() { return sender; }
    public UserTable getReceiver() { return receiver; }
    public String getContent() { return content; }
    public OffsetDateTime getSentAt() { return sentAt; }
    public boolean getIsRead() { return isRead; }

    public void setSender(UserTable sender) { this.sender = sender; }
    public void setReceiver(UserTable receiver) { this.receiver = receiver; }
    public void setContent(String content) { this.content = content; }
    public void setSentAt(OffsetDateTime sentAt) { this.sentAt = sentAt; }
    public void setRead(boolean isRead) { this.isRead = isRead; }
}
