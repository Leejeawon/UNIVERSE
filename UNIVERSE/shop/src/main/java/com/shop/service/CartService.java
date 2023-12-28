package com.shop.service;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.OrderDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    // 장바구니에 물건 추가
    public Long addCart(CartItemDto cartItemDto, String loginid){
        // 장바구니에 담을 상품을 검색
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        // 로그인한 회원을 검색
        Member member = memberRepository.findByLoginid(loginid);
        // 회원이 가지고 있는 장바구니 검색
        Cart cart = cartRepository.findByMemberId(member.getId());
        // 장바구니가 존재하지 않으면
        if (cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 장바구니의 id와 상품id를 이용하여
        // 장바구니에 이미 존재하는 상품인지 확인
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        // 장바구니에 존재하지 않는 상품이면
        if (savedCartItem != null){
            // 장바구니 수량을 증가
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            // 장바구니에 상품이 존재하지 않으면 새로 상품을 추가
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String loginid){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        // 현재 로그인한 회원의 장바구니 조회
        Member member = memberRepository.findByLoginid(loginid);
        Cart cart = cartRepository.findByMemberId(member.getId());
        // 장바구니에 상품을 한번도 담지 않았으면
        // 비어있는 리스트를 리턴
        if (cart == null){
            return cartDetailDtoList;
        }

        // 장바구니에 담겨있는 상품 정보를 조회
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
            return cartDetailDtoList;
    }

    // 로그인한 회원과 장바구니 상품을 저장한 회원이 동일한지 확인
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String loginid){

        Member curMember = memberRepository.findByLoginid(loginid);
        // 장바구니 항목 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();
        // 현재 로그인한 회원과 상품을 저장한 회원이 다르면 false 리턴
        if (!StringUtils.equals(curMember.getLoginid(), savedMember.getLoginid())){
            return false;
        }
        return true;
    }

    // 장바구니의 수량을 업데이트 하는 메서드
    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }
    // 주문을 처리하고 주문한 상품은 장바구니에서 삭제하는 메서드
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String loginid){
        List<OrderDto> orderDtoList = new ArrayList<>();
        // 장바구니 페이지에서 넘겨받은 주문 상품 번호를 이용하여
        // 주문에 전달한 orderDto 객체를 생성
        for (CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }
        // 장바구니에 담은 상품을 주문하도록 주문 메서드를 호출
        Long orderId = orderService.orders(orderDtoList, loginid);

        for (CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        // 주문 id를 반환
        return orderId;
    }
}
