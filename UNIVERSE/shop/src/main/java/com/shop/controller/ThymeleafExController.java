package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

    @GetMapping("/ex01")
    public String thymeleafExample01(Model model){
        model.addAttribute("data","타임리프 예제입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafExample02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemName("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDTO", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafExample03(Model model){
//        List<ItemDTO> itemDtoList = new ArrayList<>();
//
//        for (int i=1; i<=10; i++){
//            ItemDTO itemDto = new ItemDTO();
//            itemDto.setItemName("테스트 상품" + i);
//            itemDto.setItemDetail("상품 상세 설명" + i);
//            itemDto.setPrice(10000*i);
//            itemDto.setRegTime(LocalDateTime.now());
//
//            itemDtoList.add(itemDto);
//        }
//
//        model.addAttribute("itemDtoList", itemDtoList);
//        return "thymeleafEx/thymeleafEx03";
//
        List<ItemDto> itemDtoList = new ArrayList<>();

        //반복문을 통해 화며에서출력할 10개의 itemDto객체를 만들어서 itemDtoList에 넣어준다.
        for(int i =1; i <=10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemName("테스트상품" + i);
            itemDto.setItemDetail("테스트 상품 상세설명"+ i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);	//화면에서 출력할 itemDtoList를 model에 담아서 View에 전딜
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafExample04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i=1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemName("테스트 상품" + i);
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex05")
    public String thymeleafExample05(Model model){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String thymeleafExample06(Model model, String param1, String param2){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping("/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }
}
