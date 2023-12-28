package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

//@Entity : 클래스를 entity로 설정
//@Table : 엔티티와 매핑할 테이블 설정
@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    @Id     //테이블의 기본키
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    //상품코드

    @Column(nullable=false, length=50) //공백불가
    private String itemName;    //상품명

    @Column(name="price", nullable = false)
    private int price;      //가격

    @Column(nullable = false)
    private int stockNumber;    //재고수량

    //@Lob : BLOB, CLOB 타입 매핑
    //BLOB, CLOB : 사이즈가 큰 데이터를 외부 파일로 저장하기위한 데이터타입
    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto){
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if (restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량 : " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}
