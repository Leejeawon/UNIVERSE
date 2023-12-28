package com.shop.controller;

import com.shop.entity.Post;
import com.shop.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/list")
    public String getAllPosts(Model model, @RequestParam(name = "highlightedPostId", required = false) Long highlightedPostId) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("highlightedPostId", highlightedPostId); // Add this line

        return "posts/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, Model model) {
        Post savedPost = postService.savePost(post);
        model.addAttribute("highlightedPostId", savedPost.getId());
        return "redirect:/posts/list";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        postService.getPostById(id).ifPresent(post -> model.addAttribute("post", post));
        return "posts/edit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Post updatedPost) {
        postService.getPostById(id).ifPresent(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            postService.savePost(post);
        });
        return "redirect:/posts/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts/list";
    }
}
