package com.shop.controller;

import com.shop.entity.NoticeBoard;
import com.shop.service.NoticeBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/noticeboard")
public class NoticeBoardController {

    @Autowired
    private NoticeBoardService noticeBoardService;

    @GetMapping("/list")
    public String showList(Model model) {
        List<NoticeBoard> posts = noticeBoardService.getAllPosts();
        model.addAttribute("posts", posts);
        return "noticeboard/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("post", new NoticeBoard());
        return "noticeboard/new";
    }

    @PostMapping("/new")
    public String addPost(@ModelAttribute NoticeBoard post) {
        noticeBoardService.savePost(post);
        return "redirect:/noticeboard/list";
    }

    @GetMapping("/detail/{id}")
    public String showDetailPage(@PathVariable Long id, Model model) {
        // Retrieve the post by id from your data source (e.g., database)
        NoticeBoard post = noticeBoardService.getPostById(id);

        // Check if the post exists
        if (post != null) {
            model.addAttribute("post", post);
        } else {
            // Handle the case where the post is not found, redirect or show an error message
            // For example, you might redirect to the list page with a message
            return "redirect:/noticeboard/list?error=post_not_found";
        }

        return "noticeboard/detail"; // Assuming your detail page is named "detail.html"
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        NoticeBoard post = noticeBoardService.getPostById(id);
        model.addAttribute("post", post);
        return "noticeboard/edit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute NoticeBoard post) {
        if (noticeBoardService.verifyPassword(id, post.getPassword())) {
            noticeBoardService.savePost(post);
            return "redirect:/noticeboard/list";
        } else {
            // 비밀번호가 일치하지 않는 경우 처리
            return "redirect:/noticeboard/edit/" + id + "?error=true";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "noticeboard/delete";
    }

    @PostMapping("/delete/{id}")
    public String confirmDelete(@PathVariable Long id, @RequestParam String password) {
        if (noticeBoardService.verifyPassword(id, password)) {
            noticeBoardService.deletePost(id);
        }
        return "redirect:/noticeboard/list";
    }
}
