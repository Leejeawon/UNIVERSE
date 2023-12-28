package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.CartItemDto;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){

        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){

        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem(); // 상품 추가 메서드 호출
        Member member = saveMember(); // 사용자 생성 메서드 호출

        // 장바구니 생성하고 장바구니에 담을 수량이랑 물건 설정
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        // 장바구니에 상품을 추가
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        // 장바구니상품id를 이용하여 장바구니 상품에 대한 정보를 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        // 상품의 아이디와 장바구니에 저장된 상품 아이디가 동일한지 확인
        assertEquals(item.getId(),cartItem.getItem().getId());
        // 장바구니에 추가했던 수량과 실제 장바구니에 저장된 수량이 동일한지 확인
        assertEquals(cartItemDto.getCount(), cartItem.getCount());

    }
}
