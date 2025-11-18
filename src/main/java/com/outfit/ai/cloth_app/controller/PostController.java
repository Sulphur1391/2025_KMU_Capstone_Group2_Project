package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.CommentDto;
import com.outfit.ai.cloth_app.dto.PostDto;
import com.outfit.ai.cloth_app.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Extract UUID from current login user
    private UUID getAuthenticatedUserId() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }

        return UUID.fromString(authentication.getName());
    }

    // Check all post (GET /posts)
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPost() {
        List<PostDto> posts = postService.getAllPost();
        return ResponseEntity.ok(posts);
    }

    // Create post (POST /posts)
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) throws AccessDeniedException {
        UUID authorId = getAuthenticatedUserId();
        PostDto createdPost = postService.createPost(authorId, postDto);
        return ResponseEntity.ok(createdPost);
    }

    // Edit post (PUT /posts/{id})
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable String id, @RequestBody PostDto postDto) throws AccessDeniedException {
        UUID postId = UUID.fromString(id);
        UUID userId = getAuthenticatedUserId();

        PostDto updatedPost = postService.updatePost(postId, userId, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    // Delete post (DELETE /posts/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) throws AccessDeniedException {
        UUID postId = UUID.fromString(id);
        UUID userId = getAuthenticatedUserId();

        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // Write comment (POST /posts/{postId}/comments)
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable String postId, @RequestBody CommentDto commentDto) throws AccessDeniedException {
        UUID outfitId = UUID.fromString(postId);
        UUID userId = getAuthenticatedUserId();

        CommentDto newComment = postService.addComment(outfitId, userId, commentDto);
        return ResponseEntity.ok(newComment);
    }

    // Comment reaction (POST /posts/{postId}/comments/{commentId}/react)
    @PostMapping("/{postId}/comments/{commentId}/react")
    public ResponseEntity<CommentDto> reactComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @RequestBody String type
    ) throws AccessDeniedException {
        UUID commentUUID = UUID.fromString(commentId);
        UUID userId = getAuthenticatedUserId();

        CommentDto updatedComment = postService.reactComment(commentUUID, userId, type);
        return ResponseEntity.ok(updatedComment);
    }

    // Delete comment (DELETE /posts/{postId}/comments/{commentId})
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String postId, @PathVariable String commentId) throws AccessDeniedException {
        UUID commentUUID = UUID.fromString(commentId);
        UUID userId = getAuthenticatedUserId();

        postService.deleteComment(commentUUID, userId);
        return ResponseEntity.noContent().build();
    }
}
