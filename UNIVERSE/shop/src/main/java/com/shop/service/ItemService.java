package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록
        for(int i=0; i< itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0 )
                itemImg.setRepImgYn("Y"); // 대표이미지 여부
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    // 상품 수정
    @Transactional(readOnly = true) // 읽기전용
    public ItemFormDto getItemDtl(Long itemId){
        // 상품 이미지 목록 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // 상품 id에 해당하는 상품 이미지를 조회하여
        // itemImgDTO로 리스트에 추가함
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품 id로 상품을 조회하고 존재하지 않으면 예외 발생
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        // itemFormDTO에 상품 정보를 설정하고 목록에 추가후 리턴
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    // 상품정보, 이미지 파일 목록을 매개변수로 받아옴
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        // 상품id에 해당하는 상품을 조회
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        // 상품 정보를 변경(이름,가격,재고수량....)
        item.updateItem(itemFormDto);
        // 이미지 목록을 조회
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        // 상품이미지 파일 목록을 반복하며 업데이트 메서드 호출
        for(int i=0; i<itemImgFileList.size(); i++){
            // 상품 이미지 id, 새로운 이미지 파일 정보를 전달
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    // 상품조회 조건, 페이지 정보를 읽어서 파라미터로 조회
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDTO, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDTO, pageable);
    }

    // 메인 페이지에 보여줄 상품 데이터 조회
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
