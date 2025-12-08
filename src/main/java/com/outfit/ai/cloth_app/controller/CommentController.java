package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.CommentDto;
import com.outfit.ai.cloth_app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CommentController
 * - 댓글 작성/조회/좋아요 처리
 * - targetUserId 타입 Long으로 변경됨
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentDto createComment(
            @RequestParam Long postId,
            @RequestParam Long authorId,
            @RequestParam String content,
            @RequestParam(required = false) Long targetId   // Long으로 변경됨
    ) {
        return commentService.saveComment(postId, authorId, content, targetId);
    }

    @PostMapping("/{commentId}/like")
    public CommentDto likeComment(@PathVariable Long commentId) {
        return commentService.likeComment(commentId);
    }

    @PostMapping("/{commentId}/dislike")
    public CommentDto dislikeComment(@PathVariable Long commentId) {
        return commentService.dislikeComment(commentId);
    }

    @GetMapping
    public List<CommentDto> getComments(@RequestParam Long postId) {
        return commentService.getCommentsByPost(postId);
    }
}
