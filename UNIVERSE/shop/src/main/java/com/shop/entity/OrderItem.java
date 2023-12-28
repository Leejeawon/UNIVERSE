package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문할 상품
        orderItem.setCount(count); // 주문할 수량
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count); // 재고 감소
        return orderItem;
    }

    // 총 가격 계산
    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel(){
        this.getItem().addStock(count);
    }

}
