package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.entity.Comment;
import com.outfit.ai.cloth_app.entity.Post;
import com.outfit.ai.cloth_app.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 전체 조회
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    // 게시글 작성
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    // 댓글 좋아요
    @PostMapping("/{postId}/comments/{commentId}/like")
    public Comment likeComment(@PathVariable Long postId, @PathVariable Long commentId) {
        return postService.likeComment(postId, commentId);
    }

    // 댓글 싫어요
    @PostMapping("/{postId}/comments/{commentId}/dislike")
    public Comment dislikeComment(@PathVariable Long postId, @PathVariable Long commentId) {
        return postService.dislikeComment(postId, commentId);
    }
}
