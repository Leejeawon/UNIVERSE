package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "board_img")
@Getter @Setter
public class BoardImg extends BaseEntity{

    @Id
    @Column(name = "board_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 기본키

    private String imgName; // 이미지 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    private String repImgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board; // 아이템 엔티티

    public void updateBoardImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
