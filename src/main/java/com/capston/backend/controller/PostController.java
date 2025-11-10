package com.capston.backend.controller;

import com.capston.backend.dto.PostDto;
import com.capston.backend.dto.CommentDto;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final Map<Long, PostDto> postStore = new HashMap<>();
    private long postIdSeq = 1;
    private long commentIdSeq = 1;

    /** 게시글 목록 조회 */
    @GetMapping
    public List<PostDto> getAllPosts() {
        return new ArrayList<>(postStore.values());
    }

    /** 게시글 작성 */
    @PostMapping
    public PostDto createPost(@RequestBody PostDto post) {
        post.setId(postIdSeq++);
        postStore.put(post.getId(), post);
        return post;
    }

    /** 게시글 수정 */
    @PutMapping("/{id}")
    public PostDto updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        PostDto existingPost = postStore.get(id);
        if (existingPost == null) {
            throw new RuntimeException("게시글 없음");
        }

        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setAuthor(postDto.getAuthor());

        return existingPost;
    }

    /** 댓글 작성 */
    @PostMapping("/{postId}/comments")
    public CommentDto addComment(@PathVariable Long postId, @RequestBody CommentDto comment) {
        PostDto post = postStore.get(postId);
        if (post == null) throw new RuntimeException("게시글 없음");

        comment.setId(commentIdSeq++);
        post.getComments().add(comment);
        return comment;
    }

    /** 댓글 👍 / 👎 */
    @PostMapping("/{postId}/comments/{commentId}/react")
    public CommentDto reactComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam String type  // "like" or "dislike"
    ) {
        PostDto post = postStore.get(postId);
        if (post == null) throw new RuntimeException("게시글 없음");

        Optional<CommentDto> commentOpt = post.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst();

        if (commentOpt.isEmpty()) throw new RuntimeException("댓글 없음");

        CommentDto comment = commentOpt.get();
        if ("like".equalsIgnoreCase(type)) comment.setLikes(comment.getLikes() + 1);
        else if ("dislike".equalsIgnoreCase(type)) comment.setDislikes(comment.getDislikes() + 1);

        return comment;
    }

    /** 게시글 삭제 */
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        PostDto removed = postStore.remove(id);
        if (removed == null) return "게시글 없음";
        return "게시글 삭제 완료";
    }

    /** 댓글 삭제 */
    @DeleteMapping("/{postId}/comments/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        PostDto post = postStore.get(postId);
        if (post == null) return "게시글 없음";

        boolean removed = post.getComments().removeIf(c -> c.getId().equals(commentId));
        if (!removed) return "댓글 없음";
        return "댓글 삭제 완료";
    }
}
