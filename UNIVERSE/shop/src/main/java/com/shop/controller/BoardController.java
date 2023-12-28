package com.shop.controller;

import com.shop.dto.BoardFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping(value = "/admin/board/")
    public String boardForm(Model model){
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardForm";
    }
}
