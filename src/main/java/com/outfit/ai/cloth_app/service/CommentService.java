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

/**
 * CommentService (정리판)
 *
 * 변경 요약:
 * - targetUserId는 Long으로 받음 (Controller/DTO/Entity에 반영 필요)
 * - 댓글 좋아요 시 Post.likeCount 증가 코드를 제거 (의도 혼동 방지)
 * - 알림 전송은 NoticeService를 통해 통일
 * - 모든 API는 DTO 반환
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NoticeService noticeService;

    /**
     * 댓글 저장
     *
     * @param postId       게시글 id
     * @param authorId     작성자 id
     * @param content      댓글 내용
     * @param targetUserId 알림 받을 대상 유저 id (Long)
     * @return CommentDto
     */
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
        // NOTE: Comment.entity의 targetId 필드 타입을 String -> Long으로 변경해야 함.
        comment.setTargetId(targetUserId);

        Comment savedComment = commentRepository.save(comment);

        // 🔔 알림 전송: NotificationDto 내부 sender/target 타입은 String으로 유지 가능하나,
        // 서비스 간 타입은 Long을 사용해 통일하는 걸 권장.
        NotificationDto notification = new NotificationDto(
                "COMMENT",
                savedComment.getContent(),
                savedComment.getAuthor().getId().toString(),
                targetUserId != null ? targetUserId.toString() : null
        );

        // NoticeService는 Long을 인자로 받는 버전으로 재정의되어 있음.
        if (targetUserId != null) {
            noticeService.sendCommentNotice(targetUserId, notification);
        }

        return mapToDto(savedComment);
    }

    /**
     * 댓글 좋아요
     * - 댓글의 likes 필드만 증가시킨다.
     * - Post의 likeCount를 증가시키지 않음 (의도 혼동 방지)
     */
    public CommentDto likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
        comment.setLikes(comment.getLikes() + 1);
        Comment updated = commentRepository.save(comment);
        return mapToDto(updated);
    }

    /**
     * 댓글 싫어요
     * - 댓글의 dislikes 필드만 증가시킨다.
     */
    public CommentDto dislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
        comment.setDislikes(comment.getDislikes() + 1);
        Comment updated = commentRepository.save(comment);
        return mapToDto(updated);
    }

    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /* ----- Helper mappers ----- */

    private CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .authorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null)
                // NOTE: CommentDto.targetId 타입을 Long으로 변경 권장.
                .targetId(comment.getTargetId())
                .build();
    }

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
