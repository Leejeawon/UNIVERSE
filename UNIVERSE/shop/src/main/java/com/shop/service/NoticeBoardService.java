package com.shop.service;

import com.shop.entity.NoticeBoard;

import java.util.List;

public interface NoticeBoardService {
    List<NoticeBoard> getAllPosts();
    NoticeBoard getPostById(Long id);
    void savePost(NoticeBoard post);
    void deletePost(Long id);
    boolean verifyPassword(Long id, String password);

}

