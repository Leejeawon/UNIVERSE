package com.shop.repository;

import com.shop.dto.BoardContentDto;
import com.shop.dto.BoardSearchDto;
import com.shop.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<BoardContentDto> getBoardContentPage(BoardSearchDto boardSearchDto, Pageable pageable);

}
