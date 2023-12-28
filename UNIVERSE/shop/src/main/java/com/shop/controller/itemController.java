package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor // 생성자를 자동으로 만들어주는 어노테이션
public class itemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/{itemId}")
    // @PathVariable : URI에 전달된 변수값을 매핑하는데 사용
    // RESTAPI를 사용한 웹 서비스에서 클라이언트로부터 전달되는 변수값을 추출할 때 사용
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
//            model.addAttribute("itemFormDto", new ItemFormDto());
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    // 상품등록 폼을 보여주기 위한 메서드
    @GetMapping("/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    // 상품 등록을 처리하는 메서드
//    @Valid ItemFormDTO itemFormDto : 유효성 검사를 수행
//    BindingResult bindingResult : 유효성검사의 결과를 저장
//    Model model : 뷰에 전달할 데이터를 저장
//    @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList : 상품 이미지 파일 목록
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        // 유효성검사에서 오류가 있으면 상품 등록폼으로 이동
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        // 첫번째 이미지가 비어있고, 상품의 id가 없으면
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            // 에러메세지를 설정하고 상품등록폼으로 리턴
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            // 상품 저장
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            // 예외 발생하면 에러메세지를 전달하고 상품등록폼으로 이동
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        // 상품등록이 정상적으로 처리되었으면 메인페이지 이동
        return "redirect:/";

    }

    // 상품 관리 페이지
    // @PathVariable("page") Optional<Integer> page : 선택적으로 url 경로의 {page}의 값을 받아옴
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        // url경로에 페이지 번호가 있으면 해당 페이지를 조회
        // PageRequest.of(조회할 페이지 번호, 한번에 가져올 데이터의 수)
        // page.isPresent() : optinal객체에 값이 존재하는지 여부를 확인
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,10);
        // 조회할 페이지에 대한 조건과 페이지에 대한 정보를 매개변수로 전달하여
        // page 객체를 반환받음
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        // 조회한 상품 데이터, 검색 조건에 대한 내용, 최대 페이지의 번호를 설정하여 리턴
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){

        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);

        return "/item/itemDtl";
    }

}
