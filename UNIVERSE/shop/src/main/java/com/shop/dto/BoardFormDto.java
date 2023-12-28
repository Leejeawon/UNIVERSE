package com.shop.dto;

import com.shop.entity.Board;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class BoardFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String boardName;

    @NotBlank(message = "내용은 필수입니다.")
    private String boardDetail;

    private List<BoardImgDto> boardImgDtoList = new ArrayList<>();

    private List<Long> boardImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    public Board createBoard(){
        return modelMapper.map(this, Board.class);
    }

    public static BoardFormDto of(Board board){
        return modelMapper.map(board, BoardFormDto.class);
    }


}

