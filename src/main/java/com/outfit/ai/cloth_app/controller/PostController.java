package com.outfit.ai.cloth_app.controller;

<<<<<<< HEAD
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

// 게시글 컨트롤러
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 현재 유저 ID에서 인증 확인
    private UUID getAuthenticatedUserId() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }

        return UUID.fromString(authentication.getName());
    }

    // 모든 게시글 확인 (GET /posts)
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPost() {
        List<PostDto> posts = postService.getAllPost();
        return ResponseEntity.ok(posts);
    }

    // 게시글 생성 (POST /posts)
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) throws AccessDeniedException {
        UUID authorId = getAuthenticatedUserId();
        PostDto createdPost = postService.createPost(authorId, postDto);
        return ResponseEntity.ok(createdPost);
    }

    // 게시글 수정 (PUT /posts/{id})
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable String id, @RequestBody PostDto postDto) throws AccessDeniedException {
        UUID postId = UUID.fromString(id);
        UUID userId = getAuthenticatedUserId();

        PostDto updatedPost = postService.updatePost(postId, userId, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제 (DELETE /posts/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) throws AccessDeniedException {
        UUID postId = UUID.fromString(id);
        UUID userId = getAuthenticatedUserId();

        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 작성 (POST /posts/{postId}/comments)
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable String postId, @RequestBody CommentDto commentDto) throws AccessDeniedException {
        UUID outfitId = UUID.fromString(postId);
        UUID userId = getAuthenticatedUserId();

        CommentDto newComment = postService.addComment(outfitId, userId, commentDto);
        return ResponseEntity.ok(newComment);
    }

    // 게시글, 댓글 반응 (POST /posts/{postId}/comments/{commentId}/react)
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

    // 댓글 삭제 (DELETE /posts/{postId}/comments/{commentId})
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String postId, @PathVariable String commentId) throws AccessDeniedException {
        UUID commentUUID = UUID.fromString(commentId);
        UUID userId = getAuthenticatedUserId();

        postService.deleteComment(commentUUID, userId);
        return ResponseEntity.noContent().build();
=======
import com.outfit.ai.cloth_app.entity.Post;
import com.outfit.ai.cloth_app.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}")
    public Post getPost(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable Long postId, @RequestBody Post updated) {
        return postService.updatePost(postId, updated);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
>>>>>>> origin/main
    }
}
