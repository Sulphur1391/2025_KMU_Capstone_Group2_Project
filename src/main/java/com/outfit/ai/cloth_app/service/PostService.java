package com.outfit.ai.cloth_app.service;

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

    @Transactional(readOnly = true)
    public List<PostDto> getAllPost() {
        List<OutfitCombination> sharedOutfits = outfitRepository
                .findByIsSharedTrueOrderByCreatedAtDesc();

        return sharedOutfits.stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

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

    public void deletePost(UUID postId, UUID userId) throws AccessDeniedException {
        OutfitCombination existingOutfit = outfitRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        checkAuthorization(existingOutfit.getUserTable().getUserId(), userId, "게시글 삭제");

        outfitRepository.delete(existingOutfit);
    }

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

    public void deleteComment(UUID commentId, UUID userId) throws AccessDeniedException {
        CommunityInteractions existingComment = interactionsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        checkAuthorization(existingComment.getUserTable().getUserId(), userId, "댓글 삭제");

        interactionsRepository.delete(existingComment);
    }

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

    private CommentDto convertToCommentDtoWithCount(CommunityInteractions comment) {
        long likes = interactionsRepository
                .countByParentAndInteractionType(comment, "LIKE");

        long dislikes = interactionsRepository
                .countByParentAndInteractionType(comment, "DISLIKE");

        return CommentDto.fromEntity(comment, (int)likes, (int)dislikes);
    }

    private void checkAuthorization(UUID authorId, UUID requestId, String operation) throws AccessDeniedException {
        if (!authorId.equals(requestId)) {
            throw new AccessDeniedException(String.format("해당 %s을(를) 수행할 권한이 없습니다.", operation));
        }
    }
}
