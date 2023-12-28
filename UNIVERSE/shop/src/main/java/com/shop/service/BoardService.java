package com.shop.service;

import com.shop.dto.*;
import com.shop.entity.Board;
import com.shop.entity.BoardImg;
import com.shop.repository.BoardImgRepository;
import com.shop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImgService boardImgService;
    private final BoardImgRepository boardImgRepository;

    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception{
        // 상품 등록
        Board board = boardFormDto.createBoard();
        boardRepository.save(board);

        // 이미지 등록
        for(int i=0; i< boardImgFileList.size(); i++){
            BoardImg boardImg = new BoardImg();
            boardImg.setBoard(board);
            if(i == 0 )
                boardImg.setRepImgYn("Y"); // 대표이미지 여부
            else
                boardImg.setRepImgYn("N");
            boardImgService.saveBoardImg(boardImg, boardImgFileList.get(i));
            }

            return board.getId();
        }

        // 수정
        @Transactional(readOnly = true) // 읽기전용
        public BoardFormDto getBoardDtl(Long boardId){
            List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
            List<BoardImgDto> boardImgDtoList = new ArrayList<>();
            for (BoardImg boardImg : boardImgList){
                BoardImgDto boardImgDto = BoardImgDto.of(boardImg);
                boardImgDtoList.add(boardImgDto);
            }

            Board board = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            BoardFormDto boardFormDto = BoardFormDto.of(board);
            boardFormDto.setBoardImgDtoList(boardImgDtoList);
            return boardFormDto;
        }

        // 내용, 이미지 파일 목록을 매개변수로 받아옴
        public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception{
            // 상품id에 해당하는 상품을 조회
            Board board = boardRepository.findById(boardFormDto.getId())
                    .orElseThrow(EntityNotFoundException::new);
            // 상품 정보를 변경(이름,가격,재고수량....)
            board.updateBoard(boardFormDto);
            // 이미지 목록을 조회
            List<Long> boardImgIds = boardFormDto.getBoardImgIds();
            // 상품이미지 파일 목록을 반복하며 업데이트 메서드 호출
            for(int i=0; i<boardImgFileList.size(); i++){
                // 상품 이미지 id, 새로운 이미지 파일 정보를 전달
                boardImgService.updateBoardImg(boardImgIds.get(i), boardImgFileList.get(i));
            }

            return board.getId();
        }

        // 상품조회 조건, 페이지 정보를 읽어서 파라미터로 조회
        @Transactional(readOnly = true)
        public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDTO, Pageable pageable){
            return boardRepository.getAdminBoardPage(boardSearchDTO, pageable);
        }

        // 메인 페이지에 보여줄 상품 데이터 조회
        @Transactional(readOnly = true)
        public Page<BoardContentDto> getBoardContentPage(BoardSearchDto boardSearchDto, Pageable pageable) {
            return boardRepository.getBoardContentPage(boardSearchDto, pageable);
        }
}


