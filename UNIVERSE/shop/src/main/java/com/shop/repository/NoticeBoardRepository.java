package com.shop.repository;

import com.shop.entity.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
}

