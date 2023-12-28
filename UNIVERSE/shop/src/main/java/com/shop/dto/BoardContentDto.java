package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BoardContentDto {
        private Long id;
        private String boardName;
        private String boardDetail;
        private String imgUrl;

        @QueryProjection
        public BoardContentDto(Long id, String boardName, String boardDetail, String imgUrl){
            this.id = id;
            this.boardName = boardName;
            this.boardDetail = boardDetail;
            this.imgUrl = imgUrl;
        }
}
