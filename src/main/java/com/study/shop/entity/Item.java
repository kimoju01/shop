package com.study.shop.entity;

import com.study.shop.constant.ItemSellStatus;
import com.study.shop.dto.ItemFormDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column(name = "code", nullable = false, length = 10)
//    private String code;    // 상품 코드

    @Column(name = "name", nullable = false, length = 50)
    private String name;    // 상품 이름

    @Column(name = "price", nullable = false)
    private int price;          // 상품 가격

    @Column(name = "stock", nullable = false)
    private int stock;    // 재고 수량

    @Lob
    @Column(name = "detail", nullable = false)
    private String detail;  // 상품 설명

    @Column(name = "item_sell_status")
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태(SELL, SOLD_OUT)

    // 엔티티 상태 변경 메서드를 엔티티 클래스 내에 추가하면 좀 더 객체 지향적 설계임
    // 객체가 자체적으로 상태와 행동을 관리. 변경에 대한 책임은 객체 내부에 캡슐화 됨
    public void updateItem(ItemFormDto itemFormDto) {
        // 상품 정보 업데이트
        this.name = itemFormDto.getName();
        this.price = itemFormDto.getPrice();
        this.stock = itemFormDto.getStock();
        this.detail = itemFormDto.getDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

}
