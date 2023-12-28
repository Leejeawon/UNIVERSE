package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String loginid){
        // 주문하고자하는 상품을 조회
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow((EntityNotFoundException::new));
        // 이메일을 이용하여 주문 회원을 확인
        Member member = memberRepository.findByLoginid(loginid);

        // 주문 상품과 주문수량을 이용하여 주문 리스트에 추가
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // 주문을 생성
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){

        // 사용자의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);
        // 전체 주문 건수를 조회
        Long totalCount = orderRepository.countOrder(email);
        // 주문 내역을 저장할 리스트 설정
        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        // 주문에 대해서 OrderHistDto를 생성하고 리스트에 추가
        for (Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            // 각 주문에 속한 주문 상품에 대해서 OrderItemDto를 생성하고 리스트에 추가
            for (OrderItem orderItem : orderItems){
                // 주문 상품의 대표 이미지를 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                // 대표 이미지를 설정
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                // orderItemDto를 orderHistDto에 추가
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            // 생성된 orderHistDto를 리스트에 추가
            orderHistDtos.add(orderHistDto);
        }
        // Page 객체로 변환하여 반환
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    // 주문자와 취소하려는 사람이 동일한지 검사
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String loginid){
        // 현재 이메일 주소를 이용하여 회원정보를 조회
        Member curMember = memberRepository.findByLoginid(loginid);
        // 주문 id를 이용하여 주문 정보를 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        // 주문한 회원의 정보를 저장
        Member saveMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())){
            return false;
        }
        return true;
    }

    // 주문 아이디를 넘겨받아서 취소처리
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String loginid){
        // 로그인한 회원의 정보를 가져옴
        Member member = memberRepository.findByLoginid(loginid);
        List<OrderItem> orderItemList = new ArrayList<>();
        // 주문할 상품의 목록을 저장
        for (OrderDto orderDto : orderDtoList){
            // 주문상품 id를 이용하여 상품을 검색
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);
            // 상품과 수량으로 orderItem을 생성하여
            // 주문 상품 목록에 추가
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
                orderItemList.add(orderItem);
        }
        // 현재 로그인한 회원과 주문 상품 목록을 이용하여 주문 엔티티를 생성
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
