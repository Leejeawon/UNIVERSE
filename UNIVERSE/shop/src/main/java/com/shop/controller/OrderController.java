package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // @ResponseBody : HTTP요청의 본문에 담긴 내용을 Json형태로 자바 객체로 변환하여 전달
    // @RequestBody : 자바 객체를 HTTP요청의 본문으로 전달
    // Principal : 스프링 시큐리티에서 제공하는 객체
    //             로그인이 되어 있으면 로그인 계정에 대한 정보를 담음
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order (@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){

        // 유효성 검사에 오류가 있으면
        if (bindingResult.hasErrors()){
            // 에러에 대한 정보(bindingResult의 filedErrors)를 가져와서
            // 에러메세지를 StringBuilder에 추가
            // filedErrors : bindingResult에 보관되고 있는 오류 객체
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            // 에러메세지를 하나의 문자열로 처리하고,
            // BAD_REQUEST 상태와 함께 리턴 처리
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        // 현재 로그인된 사용자의 이메일 정보를 가져옴
        String email = principal.getName();
        Long orderId;

        try {
            // 주문정보와 회원의 이메일을 이용하여
            // 주문을 처리하고 주문id를 받환받음
            orderId = orderService.order(orderDto, email);
        } catch (Exception e){
            // 예외가 발생되면 에러메세지와 BAD_REQUEST를 리턴
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        // 주문이 정상적으로 처리가 된 경우에는
        // 주문번호와, 상태코드 OK로 리턴
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        // 페이징
        // 현재 로그인한 회원의 이메일과 페이징을 이용하여
        // 주문 목록 데이터를 리턴함
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        // 주문 목록, 페이징처리, 페이징최대값
        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){

        if (!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
