package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.dto.CommentDto;
import com.outfit.ai.cloth_app.dto.NotificationDto;
import com.outfit.ai.cloth_app.dto.PostDto;
import com.outfit.ai.cloth_app.entity.Comment;
import com.outfit.ai.cloth_app.entity.Post;
import com.outfit.ai.cloth_app.entity.User;
import com.outfit.ai.cloth_app.repository.AppUserRepository; // 변경
import com.outfit.ai.cloth_app.repository.CommentRepository;
import com.outfit.ai.cloth_app.repository.PostRepository;
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
    private final AppUserRepository userRepository; // 변경
    private final NoticeService noticeService;

    // 기존 메서드 그대로 사용
    public CommentDto saveComment(Long postId, Long authorId, String content, Long targetUserId) {
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
        comment.setTargetId(targetUserId);

        Comment savedComment = commentRepository.save(comment);

        NotificationDto notification = new NotificationDto(
                "COMMENT",
                savedComment.getContent(),
                savedComment.getAuthor().getId().toString(),
                targetUserId != null ? targetUserId.toString() : null
        );

        if (targetUserId != null) {
            noticeService.sendCommentNotice(targetUserId, notification);
        }

        return mapToDto(savedComment);
    }

    //  추가 필요: 좋아요
    public CommentDto likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
        comment.setLikes(comment.getLikes() + 1);
        return mapToDto(commentRepository.save(comment));
    }

    //  추가 필요: 싫어요
    public CommentDto dislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
        comment.setDislikes(comment.getDislikes() + 1);
        return mapToDto(commentRepository.save(comment));
    }

    //  추가 필요: 게시글별 댓글 조회
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
}
