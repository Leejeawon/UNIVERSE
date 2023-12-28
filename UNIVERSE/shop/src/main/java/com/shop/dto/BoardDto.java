package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
    @Getter
    @Setter
    public class BoardDto {

        private Long id;
        private String boardName;
        private String boardDetail;
        private LocalDateTime regTime;
        private LocalDateTime updateTime;
}
