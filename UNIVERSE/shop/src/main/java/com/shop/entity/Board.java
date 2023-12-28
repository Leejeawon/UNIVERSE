package com.shop.entity;

import com.shop.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

    @Entity
    @Table(name="board")
    @Getter
    @Setter
    @ToString
    public class Board extends BaseEntity{
        @Id
        @Column(name="board_id")
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @Column(nullable=false, length=50) //공백불가
        private String boardName;    // 제목

        @Lob
        @Column(nullable = false)
        private String boardDetail;  // 게시판 내용

        public void updateBoard(BoardFormDto boardFormDto){
            this.boardName = boardFormDto.getBoardName();
            this.boardDetail = boardFormDto.getBoardDetail();
        }
}
