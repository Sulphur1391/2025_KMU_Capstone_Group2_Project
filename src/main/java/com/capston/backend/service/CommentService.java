package com.capston.backend.service;

import com.capston.backend.entity.Comment;
import com.capston.backend.entity.Post;
import com.capston.backend.repository.CommentRepository;
import com.capston.backend.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    /**
     * 댓글 저장 + Post 연결
     */
    public Comment saveComment(Long postId, String senderId, String content, String targetId) {
        // 1️⃣ 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        // 2️⃣ 댓글 생성
        Comment comment = new Comment(content);
        comment.setPost(post);

        // 3️⃣ DB 저장
        Comment savedComment = commentRepository.save(comment);

        // 4️⃣ Post에 댓글 추가
        post.getComments().add(savedComment);
        postRepository.save(post);

        return savedComment;
    }
}
