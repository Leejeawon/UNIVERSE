package com.shop.controller;

import com.shop.dto.BoardFormDto;
import com.shop.dto.BoardSearchDto;
import com.shop.entity.Board;
import com.shop.service.BoardService;
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
@RequiredArgsConstructor
public class ContentController {

        private final BoardService boardService;

        @GetMapping(value = "/admin/board/{boardId}")
        public String boardDtl(@PathVariable("boardId") Long boardId, Model model){

            try {
                BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
                model.addAttribute("boardFormDto", boardFormDto);
            } catch (EntityNotFoundException e){
                model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
                model.addAttribute("boardFormDto", new BoardFormDto());
                return "board/boardForm";
            }
            return "board/boardForm";
        }

        @PostMapping(value = "/admin/board/{boardId}")
        public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList, Model model){
            if (bindingResult.hasErrors()){
                return "board/boardForm";
            }

            if (boardImgFileList.get(0).isEmpty() && boardFormDto.getId() == null){
                model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
                return "board/boardForm";
            }

            try {
                boardService.updateBoard(boardFormDto, boardImgFileList);
            } catch (Exception e){
                model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
                return "board/boardForm";
            }
            return "redirect:/";
        }

        // 상품등록 폼을 보여주기 위한 메서드
        @GetMapping("/admin/board/new")
        public String boardForm(Model model){
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "/board/boardForm";
        }

        // 상품 등록을 처리하는 메서드
//    @Valid ItemFormDTO itemFormDto : 유효성 검사를 수행
//    BindingResult bindingResult : 유효성검사의 결과를 저장
//    Model model : 뷰에 전달할 데이터를 저장
//    @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList : 상품 이미지 파일 목록
        @PostMapping(value = "/admin/board/new")
        public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model, @RequestParam("boardImgFile")List<MultipartFile> boardImgFileList){

            // 유효성검사에서 오류가 있으면 상품 등록폼으로 이동
            if (bindingResult.hasErrors()){
                return "board/boardForm";
            }

            // 첫번째 이미지가 비어있고, 상품의 id가 없으면
            if (boardImgFileList.get(0).isEmpty() && boardFormDto.getId() == null){
                // 에러메세지를 설정하고 상품등록폼으로 리턴
                model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
                return "board/boardForm";
            }

            try {
                // 상품 저장
                boardService.saveBoard(boardFormDto, boardImgFileList);
            } catch (Exception e){
                // 예외 발생하면 에러메세지를 전달하고 상품등록폼으로 이동
                model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
                return "board/boardForm";
            }
            // 상품등록이 정상적으로 처리되었으면 메인페이지 이동
            return "redirect:/";

        }

        // 상품 관리 페이지
        // @PathVariable("page") Optional<Integer> page : 선택적으로 url 경로의 {page}의 값을 받아옴
        @GetMapping(value = {"/admin/boards", "/admin/boards/{page}"})
        public String boardManage(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
            // url경로에 페이지 번호가 있으면 해당 페이지를 조회
            // PageRequest.of(조회할 페이지 번호, 한번에 가져올 데이터의 수)
            // page.isPresent() : optinal객체에 값이 존재하는지 여부를 확인
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,10);
            // 조회할 페이지에 대한 조건과 페이지에 대한 정보를 매개변수로 전달하여
            // page 객체를 반환받음
            Page<Board> boards = boardService.getAdminBoardPage(boardSearchDto, pageable);
            // 조회한 상품 데이터, 검색 조건에 대한 내용, 최대 페이지의 번호를 설정하여 리턴
            model.addAttribute("boards", boards);
            model.addAttribute("boardSearchDto", boardSearchDto);
            model.addAttribute("maxPage", 5);

            return "board/boardMng";
        }

        @GetMapping(value = "/board/{boardId}")
        public String boardDtl(Model model, @PathVariable("boardId") Long boardId){

            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("board", boardFormDto);

            return "/board/boardDtl";
        }
}
