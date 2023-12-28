package com.shop.repository;

import com.shop.entity.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {

        List<BoardImg> findByBoardIdOrderByIdAsc(Long boardId);

        // 상품의 대표이미지를 검색
        BoardImg findByBoardIdAndRepImgYn(Long boardId, String repImgYn);
}
