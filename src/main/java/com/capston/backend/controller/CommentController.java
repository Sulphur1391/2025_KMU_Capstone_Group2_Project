package com.capston.backend.controller;

import com.capston.backend.dto.NotificationDto;
import com.capston.backend.entity.Comment;
import com.capston.backend.service.CommentService;
import com.capston.backend.service.NoticeService;
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
