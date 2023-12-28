package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class NoticeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 시퀀스로 생성되는 글번호

    private String title;
    private String content;
    private String password;

}

