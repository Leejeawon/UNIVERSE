package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg){
        // modelMapper.map(매핑 되는 객체, 매핑 결과로 생성할 객체의 클래스)
        return modelMapper.map(itemImg, ItemImgDto.class);
    }

}
