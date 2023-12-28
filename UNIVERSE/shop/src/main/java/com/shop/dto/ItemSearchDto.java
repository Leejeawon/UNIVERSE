package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType; // 현재시간, 상품등록일을 비교해서 조회
    private ItemSellStatus searchSellStatus; // 판매 상태
    private String searchBy; // 상품 조회 유형
    private String searchQuery=""; // 검색어 저장
}
