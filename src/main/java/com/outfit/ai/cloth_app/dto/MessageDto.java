package com.outfit.ai.cloth_app.dto;

import com.outfit.ai.cloth_app.entity.Message;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .content(message.getContent())
                .build();
    }
}
