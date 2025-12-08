package com.outfit.ai.cloth_app.service;

<<<<<<< HEAD
import com.outfit.ai.cloth_app.dto.CommentDto;
import com.outfit.ai.cloth_app.dto.PostDto;
import com.outfit.ai.cloth_app.repository.CommunityInteractionsRepository;
import com.outfit.ai.cloth_app.repository.OutfitCombinationRepository;
import com.outfit.ai.cloth_app.repository.UserRepository;
import com.outfit.ai.cloth_app.tables.CommunityInteractions;
import com.outfit.ai.cloth_app.tables.OutfitCombination;
import com.outfit.ai.cloth_app.tables.UserTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// 게시글 등록 서비스
@Service
@Transactional
public class PostService {
    private final OutfitCombinationRepository outfitRepository;
    private final CommunityInteractionsRepository interactionsRepository;
    private final UserRepository userRepository;

    public PostService(
            OutfitCombinationRepository outfitRepository,
            CommunityInteractionsRepository interactionsRepository,
            UserRepository userRepository) {
        this.outfitRepository = outfitRepository;
        this.interactionsRepository = interactionsRepository;
        this.userRepository = userRepository;
    }

    // 모든 게시글 불러오기
    @Transactional(readOnly = true)
    public List<PostDto> getAllPost() {
        List<OutfitCombination> sharedOutfits = outfitRepository
                .findByIsSharedTrueOrderByCreatedAtDesc();

        return sharedOutfits.stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    // 게시글 생성
    public PostDto createPost(UUID authorId, PostDto postDto) {
        UserTable author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("작성자를 찾을 수 없습니다."));

        OutfitCombination newOutfit = new OutfitCombination();
        newOutfit.setUserTable(author);
        newOutfit.setShared(true);
        newOutfit.setAiGenImageUrl(postDto.getImageUrl());

        OutfitCombination savedOutfit = outfitRepository.save(newOutfit);

        if (postDto.getContent() != null && !postDto.getContent().trim().isEmpty()) {
            CommunityInteractions postBody = new CommunityInteractions();
            postBody.setOutfitCombination(savedOutfit);
            postBody.setUserTable(author);
            postBody.setInteractionType("POST_BODY");
            postBody.setContent(postDto.getContent());

            interactionsRepository.save(postBody);
        }

        return convertToPostDto(savedOutfit);
    }

    // 게시글 수정
    public PostDto updatePost(UUID postId, UUID userId, PostDto postDto) throws AccessDeniedException {
        OutfitCombination existingOutfit = outfitRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        checkAuthorization(existingOutfit.getUserTable().getUserId(), userId, "게시글 수정");

        existingOutfit.setShared(postDto.getIsShared());
        existingOutfit.setAiGenImageUrl(postDto.getImageUrl());

        interactionsRepository
                .findByOutfitCombinationAndInteractionTypeOrderByCreatedAtAsc(existingOutfit, "POST_BODY")
                .ifPresentOrElse(
                        bodyInteraction -> bodyInteraction.setContent(postDto.getContent()),
                        () -> {
                            if (postDto.getContent() != null && !postDto.getContent().trim().isEmpty()) {
                                CommunityInteractions newBody = new CommunityInteractions();
                                newBody.setOutfitCombination(existingOutfit);
                                newBody.setUserTable(existingOutfit.getUserTable());
                                newBody.setInteractionType("POST_BODY");
                                newBody.setContent(postDto.getContent());
                                interactionsRepository.save(newBody);
                            }
                        }
                );

        return convertToPostDto(outfitRepository.save(existingOutfit));
    }

    // 게시글 삭제
    public void deletePost(UUID postId, UUID userId) throws AccessDeniedException {
        OutfitCombination existingOutfit = outfitRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        checkAuthorization(existingOutfit.getUserTable().getUserId(), userId, "게시글 삭제");

        outfitRepository.delete(existingOutfit);
    }

    // 댓글 달기
    public CommentDto addComment(UUID postId, UUID authorId, CommentDto commentDto) {
        OutfitCombination post = outfitRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        UserTable author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("댓글 작성자를 찾을 수 없습니다."));

        CommunityInteractions comment = new CommunityInteractions();
        comment.setOutfitCombination(post);
        comment.setUserTable(author);
        comment.setInteractionType("COMMENT");
        comment.setContent(commentDto.getContent());

        CommunityInteractions savedComment = interactionsRepository.save(comment);

        return CommentDto.fromEntity(savedComment, 0, 0);
    }

    // 게시글, 댓글에 반응하기
    public CommentDto reactComment(UUID commentId, UUID userId, String type) {
        CommunityInteractions parentComment = interactionsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String interactionType = type.equalsIgnoreCase("like") ? "LIKE" : "DISLIKE";

        CommunityInteractions reaction = new CommunityInteractions();
        reaction.setParentId(parentComment);
        reaction.setUserTable(user);
        reaction.setInteractionType(interactionType);

        interactionsRepository.save(reaction);

        return convertToCommentDtoWithCount(parentComment);
    }

    // 댓글 삭제
    public void deleteComment(UUID commentId, UUID userId) throws AccessDeniedException {
        CommunityInteractions existingComment = interactionsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        checkAuthorization(existingComment.getUserTable().getUserId(), userId, "댓글 삭제");

        interactionsRepository.delete(existingComment);
    }

    // 게시글로 변환하는 DTO
    private PostDto convertToPostDto(OutfitCombination outfit) {
        PostDto dto = PostDto.fromEntity(outfit);

        Optional<CommunityInteractions> comments = interactionsRepository
                .findByOutfitCombinationAndInteractionTypeOrderByCreatedAtAsc(outfit, "COMMENT");

        List<CommentDto> commentDtos = comments.stream()
                .map(this::convertToCommentDtoWithCount)
                .collect(Collectors.toList());

        dto.setComments(commentDtos);
        return dto;
    }

    // 좋아요, 싫어요 카운트 변환하는 DTO
    private CommentDto convertToCommentDtoWithCount(CommunityInteractions comment) {
        long likes = interactionsRepository
                .countByParentAndInteractionType(comment, "LIKE");

        long dislikes = interactionsRepository
                .countByParentAndInteractionType(comment, "DISLIKE");

        return CommentDto.fromEntity(comment, (int)likes, (int)dislikes);
    }

    // 권환(게시글 수정, 삭제 등) 확인
    private void checkAuthorization(UUID authorId, UUID requestId, String operation) throws AccessDeniedException {
        if (!authorId.equals(requestId)) {
            throw new AccessDeniedException(String.format("해당 %s을(를) 수행할 권한이 없습니다.", operation));
        }
=======
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
>>>>>>> origin/main
    }
}
