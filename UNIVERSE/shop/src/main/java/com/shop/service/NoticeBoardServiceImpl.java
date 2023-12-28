package com.shop.service;

import com.shop.entity.NoticeBoard;
import com.shop.repository.NoticeBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeBoardServiceImpl implements NoticeBoardService {

    @Autowired
    private NoticeBoardRepository noticeBoardRepository;

    @Override
    public List<NoticeBoard> getAllPosts() {
        return noticeBoardRepository.findAll();
    }

    @Override
    public NoticeBoard getPostById(Long id) {
        return noticeBoardRepository.findById(id).orElse(null);
    }

    @Override
    public void savePost(NoticeBoard post) {
        noticeBoardRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        noticeBoardRepository.deleteById(id);
    }

    @Override
    public boolean verifyPassword(Long id, String password) {
        NoticeBoard post = noticeBoardRepository.findById(id).orElse(null);
        return post != null && post.getPassword().equals(password);
    }
}

