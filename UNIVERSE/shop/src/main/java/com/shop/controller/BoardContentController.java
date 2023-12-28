package com.shop.controller;

import com.shop.dto.BoardContentDto;
import com.shop.dto.BoardSearchDto;
import com.shop.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
@Controller
@RequiredArgsConstructor
public class BoardContentController {

        private final BoardService boardService;

        @GetMapping(value="/")
        public String boardContent(BoardSearchDto boardSearchDto, Optional<Integer> page, Model model){
            // 페이지 번호를 출력하기 위한 계산
            // 페이지가 존재하면 해당 페이지를 사용하고 존재하지 않으면 0페이지
            // 한 페이지당 6개씩 출력
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
            Page<BoardContentDto> boards = boardService.getBoardContentPage(boardSearchDto, pageable);
            // 뷰로 전달하기 위해 모델에 추가
            model.addAttribute("boards", boards);
            model.addAttribute("boardSearchDto", boardSearchDto);
            model.addAttribute("maxPage", 5);
            return "/index";
        }
}
