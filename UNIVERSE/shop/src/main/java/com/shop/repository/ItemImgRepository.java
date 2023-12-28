package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    // 상품의 대표이미지를 검색
    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);

}
