package com.outfit.ai.cloth_app.dto.response;

import java.util.List;

public class ClothesListResponseDto {
    private List<ClothesItemDto> clothes;
    private int totalCount;

    public List<ClothesItemDto> getClothes() { return clothes; }
    public int getTotalCount() { return totalCount; }

    public void setClothes(List<ClothesItemDto> clothes) { this.clothes = clothes; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
}
