package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.dto.request.ClothesCreateRequestDto;
import com.outfit.ai.cloth_app.dto.response.ClothesItemDto;
import com.outfit.ai.cloth_app.dto.response.ClothesListResponseDto;
import com.outfit.ai.cloth_app.repository.*;
import com.outfit.ai.cloth_app.tables.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final S3FileUploader s3FileUploader;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final MaterialRepository materialRepository;

    public ClothesService(ClothesRepository clothesRepository,
                          S3FileUploader s3FileUploader,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository,
                          ColorRepository colorRepository,
                          MaterialRepository materialRepository) {
        this.clothesRepository = clothesRepository;
        this.s3FileUploader = s3FileUploader;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.materialRepository = materialRepository;
    }

    @Transactional
    public void createClothes(UUID userId, ClothesCreateRequestDto request, MultipartFile imageFile) {
        String imageUrl = s3FileUploader.upload(imageFile, userId);

        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));

        CategoryCode categoryCode = categoryRepository.findByCategoryName(request.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 이름입니다."));

        ColorCode colorCode = colorRepository.findByColorName(request.getColorName())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 컬러 이름입니다."));

        MaterialCode materialCode = materialRepository.findByMaterialName(request.getMaterialName())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 소재 이름입니다."));

        ClothesTable newClothes = new ClothesTable();
        newClothes.setImageUrl(imageUrl);
        newClothes.setClothName(request.getName());

        newClothes.setUserTable(user);
        newClothes.setCategoryCode(categoryCode);
        newClothes.setColorCode(colorCode);
        newClothes.setMaterialCode(materialCode);

        clothesRepository.save(newClothes);
    }

    @Transactional(readOnly = true)
    public ClothesListResponseDto getClothesList(UUID userId) {
        List<ClothesTable> clothList = clothesRepository.findAllByUserTable_UserId(userId);

        List<ClothesItemDto> dtoList = clothList.stream()
                .map(this::convertToDto)
                .toList();

        ClothesListResponseDto response = new ClothesListResponseDto();
        response.setClothes(dtoList);
        response.setTotalCount(dtoList.size());

        return response;
    }

    private ClothesItemDto convertToDto(ClothesTable entity) {
        CategoryCode categoryCode = entity.getCategoryCode();
        ColorCode colorCode = entity.getColorCode();
        MaterialCode materialCode = entity.getMaterialCode();

        ClothesItemDto dto = new ClothesItemDto();
        dto.setClothID(entity.getClothId());
        dto.setName(entity.getClothName());
        dto.setCategoryName(categoryCode.getCategoryName());
        dto.setColorName(colorCode.getColorName());
        dto.setMaterialName(materialCode.getMaterialName());
        dto.setImageUrl(entity.getImageUrl());
        dto.setStyleVector(entity.getStyleVector());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
