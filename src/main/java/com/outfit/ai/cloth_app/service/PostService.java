package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.entity.Post;
import com.outfit.ai.cloth_app.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PostService (간단 정리)
 * - 게시글 관련 기본 CRUD 제공
 * - 주의: author 변경은 특별한 경우에만 허용 (권한 체크 필요)
 */
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long postId, Post updatedPost) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());

        // 주의: author 변경은 신중히 (권한 체크 필요)
        if (updatedPost.getAuthor() != null) {
            post.setAuthor(updatedPost.getAuthor());
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("게시글 없음");
        }
        postRepository.deleteById(postId);
    }
}
