package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.PostDto;
import com.outfit.ai.cloth_app.entity.Post;
import com.outfit.ai.cloth_app.entity.User;
import com.outfit.ai.cloth_app.service.PostService;
import com.outfit.ai.cloth_app.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors; // [수정] Stream 사용을 위한 임포트

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    public PostController(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    // [수정] 게시글 전체 조회: Post Entity -> PostDto 리스트 반환
    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts().stream()
                .map(this::convertToDto) // Entity를 DTO로 변환
                .collect(Collectors.toList());
    }

    // [수정] 게시글 단건 조회: Post Entity -> PostDto 반환
    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return convertToDto(post);
    }

    // [수정] 게시글 작성: 반환 타입을 Post Entity -> PostDto로 변경
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestParam Long authorId, @RequestBody Post postPayload) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("작성자 없음"));
        Post post = new Post(postPayload.getTitle(), postPayload.getContent(), author);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(convertToDto(createdPost)); // DTO 반환
    }

    // [수정] 게시글 수정: 반환 타입을 Post Entity -> PostDto로 변경
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id,
                                              @RequestBody Post updatedPostPayload,
                                              @RequestParam(required = false) Long authorId) {
        // authorId가 주어지면 author도 변경 가능 (비즈니스 로직에 따라 이 부분은 삭제 고려)
        if (authorId != null) {
            User author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("작성자 없음"));
            updatedPostPayload.setAuthor(author);
        }
        Post updated = postService.updatePost(id, updatedPostPayload);
        return ResponseEntity.ok(convertToDto(updated)); // DTO 반환
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // [추가] Post Entity를 PostDto로 매핑하는 헬퍼 메서드
    private PostDto convertToDto(Post post) {
        // [수정] Entity에서 필요한 필드만 추출하여 DTO를 빌드 (무한 재귀 방지)
        return PostDto.builder()
                .id(post.getId())
                .authorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
                .authorName(post.getAuthor() != null ? post.getAuthor().getUsername() : null)
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .createdAt(post.getCreatedAt() != null ? post.getCreatedAt().toString() : null)
                .build();
    }
}