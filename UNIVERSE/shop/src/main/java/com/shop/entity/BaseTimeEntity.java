package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// EntityListeners : 엔티티의 생명주기(생성,수정,소멸)에 대한 리스너를 지정
// MappedSuperclass : 부모클래스로만 사용되며, 실제 클래스와 매핑되지 않는 엔티티를 정의할 때 사용
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter @Setter
public abstract class BaseTimeEntity {

    @CreatedDate // Entity가 생성되고 저장될 때 시간이 자동으로 저장
    @Column(updatable = false) // 변경 불가
    private LocalDateTime regTime;

    @LastModifiedDate // Entity가 변경될 때 시간이 자동으로 저장
    private LocalDateTime updateTime;

}
