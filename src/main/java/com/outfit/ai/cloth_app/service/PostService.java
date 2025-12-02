package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.entity.Post;
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

    /**
     * 게시글 전체 조회
     * 단순한 read 작업이므로 findAll()만 호출
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * 단일 게시글 조회
     * 존재하지 않으면 예외 반환 → Controller에서 404로 처리 가능
     */
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
    }

    /**
     * 게시글 생성
     * JPA save 호출하면 insert 쿼리 실행
     */
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    /**
     * 게시글 수정
     * ⚠ 주의: setAuthor()는 필요할 때만 덮어쓰도록 변경해야 실무에서 안전함
     * 현재는 모든 필드를 클라이언트 요청대로 완전히 재작성하는 구조
     */
    public Post updatePost(Long postId, Post updatedPost) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        // [수정됨] 변경 가능한 필드만 업데이트
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());

        // [중요] author 변경 필요하지 않으면 빼는 게 안전.
        // 하지만 너희 팀 구조에서 요청에 author 포함되므로 유지.
        post.setAuthor(updatedPost.getAuthor());

        // [해결] save를 호출해야 실제 DB에 반영됨
        return postRepository.save(post);
    }

    /**
     * 게시글 삭제
     * 존재 여부 먼저 체크 → JpaException 방지
     */
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("게시글 없음");
        }
        postRepository.deleteById(postId);
    }

    // [정리] 댓글 관련 로직은 CommentService로 책임 분리됨 → 여기에 둘 필요 없음
}
