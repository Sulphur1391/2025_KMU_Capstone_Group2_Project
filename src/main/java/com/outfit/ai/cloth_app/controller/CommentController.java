package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.NotificationDto;
import com.outfit.ai.cloth_app.entity.Comment;
import com.outfit.ai.cloth_app.service.CommentService;
import com.outfit.ai.cloth_app.service.NoticeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final NoticeService noticeService;

    public CommentController(CommentService commentService, NoticeService noticeService) {
        this.commentService = commentService;
        this.noticeService = noticeService;
    }

    /**
     * 댓글 작성 + 실시간 알림 발송
     */
    @PostMapping("/add")
    public Comment addComment(@RequestParam Long postId,
                              @RequestParam String senderId,
                              @RequestParam String content,
                              @RequestParam String targetId) {
        // 1️⃣ DB에 댓글 저장
        Comment savedComment = commentService.saveComment(postId, senderId, content, targetId);

        // 2️⃣ NoticeService 통해 실시간 알림 전송
        NotificationDto notification = new NotificationDto(
                "COMMENT",
                content,
                senderId,
                targetId
        );
        noticeService.sendCommentNotice(targetId, notification);

        return savedComment;
    }
}
