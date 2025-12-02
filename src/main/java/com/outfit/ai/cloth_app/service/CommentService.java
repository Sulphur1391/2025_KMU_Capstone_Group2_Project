package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.dto.CommentDto;
import com.outfit.ai.cloth_app.dto.NotificationDto;
import com.outfit.ai.cloth_app.dto.PostDto;
import com.outfit.ai.cloth_app.entity.Comment;
import com.outfit.ai.cloth_app.entity.Post;
import com.outfit.ai.cloth_app.entity.User;
import com.outfit.ai.cloth_app.repository.CommentRepository;
import com.outfit.ai.cloth_app.repository.PostRepository;
import com.outfit.ai.cloth_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NoticeService noticeService;

    // 댓글 저장
    public CommentDto saveComment(Long postId, Long authorId, String content, String targetId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "작성자가 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setLikes(0);
        comment.setDislikes(0);
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setTargetId(targetId);

        Comment savedComment = commentRepository.save(comment);

        // 🔔 알림 전송
        NotificationDto notification = new NotificationDto(
                "COMMENT",
                savedComment.getContent(),
                savedComment.getAuthor().getId().toString(),
                targetId
        );
        noticeService.sendCommentNotice(targetId, notification);

        return mapToDto(savedComment);
    }

    public CommentDto likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
        comment.setLikes(comment.getLikes() + 1);
        Comment updated = commentRepository.save(comment);

        // 🔹 Post likeCount도 증가시키기
        Post post = updated.getPost();
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        return mapToDto(updated);
    }

    public CommentDto dislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
        comment.setDislikes(comment.getDislikes() + 1);
        Comment updated = commentRepository.save(comment);

        // 🔹 Post dislikeCount도 증가시키기
        Post post = updated.getPost();
        post.setDislikeCount(post.getDislikeCount() + 1);
        postRepository.save(post);

        return mapToDto(updated);
    }

    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .authorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null)
                .targetId(comment.getTargetId())
                .build();
    }

    // 🔹 PostDto 매핑 예시
    private PostDto mapPostToDto(Post post) {
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
