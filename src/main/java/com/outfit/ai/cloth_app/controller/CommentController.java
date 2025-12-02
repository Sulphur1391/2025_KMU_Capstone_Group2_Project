package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.CommentDto;
import com.outfit.ai.cloth_app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @RequestParam Long authorId, // [수정] String -> Long
            @RequestParam String content,
            @RequestParam String targetId
    ) {
        return ResponseEntity.ok(commentService.saveComment(postId, authorId, content, targetId));
    }

    @PostMapping("/like/{commentId}")
    public ResponseEntity<CommentDto> likeComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.likeComment(commentId));
    }

    @PostMapping("/dislike/{commentId}")
    public ResponseEntity<CommentDto> dislikeComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.dislikeComment(commentId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }
}