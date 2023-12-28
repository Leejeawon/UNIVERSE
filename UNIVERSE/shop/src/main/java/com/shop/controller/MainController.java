package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
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
public class MainController {

    private final ItemService itemService;

    @GetMapping(value="/shopping")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        // 페이지 번호를 출력하기 위한 계산
        // 페이지가 존재하면 해당 페이지를 사용하고 존재하지 않으면 0페이지
        // 한 페이지당 6개씩 출력
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        // itemService를 사용하여 아이템을 가져옴
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
            // 뷰로 전달하기 위해 모델에 추가
            model.addAttribute("items", items);
            model.addAttribute("itemSearchDto", itemSearchDto);
            model.addAttribute("maxPage", 5);
        return "/shopping/shoppingindex";
    }
}
