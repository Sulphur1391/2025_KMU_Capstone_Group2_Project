package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.entity.Post;
import com.outfit.ai.cloth_app.entity.Comment;
import com.outfit.ai.cloth_app.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /** 게시글 전체 조회 */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /** 단일 게시글 조회 */
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
    }

    /** 게시글 생성 */
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    /** 게시글 수정 */
    public Post updatePost(Long postId, Post updatedPost) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setAuthor(updatedPost.getAuthor()); // ✅ 이제 빌드 통과
        return postRepository.save(post);
    }

    /** 게시글 삭제 */
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("게시글 없음");
        }
        postRepository.deleteById(postId);
    }

    /** 댓글 추가 */
    public Comment addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        comment.setPost(post); // 댓글에 Post 연결
        post.getComments().add(comment);
        postRepository.save(post); // 댓글 반영
        return comment;
    }

    /** 댓글 삭제 */
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        boolean removed = post.getComments().removeIf(c -> c.getId().equals(commentId));
        if (!removed) throw new RuntimeException("댓글 없음");

        postRepository.save(post);
    }

    /** 댓글 좋아요 */
    public Comment likeComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        comment.setLikes(comment.getLikes() + 1);
        postRepository.save(post);
        return comment;
    }

    /** 댓글 싫어요 */
    public Comment dislikeComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        comment.setDislikes(comment.getDislikes() + 1);
        postRepository.save(post);
        return comment;
    }
}
