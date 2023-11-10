package com.study.shop.entity;

import com.study.shop.constant.ItemSellStauts;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "itemCode", nullable = false, length = 10)
    private String itemCode;    // 상품 코드

    @Column(name = "itemName", nullable = false, length = 50)
    private String itemName;    // 상품 이름

    @Column(name = "price", nullable = false)
    private int price;          // 상품 가격

    @Column(name = "stockNumber", nullable = false)
    private int stockNumber;    // 재고 수량

    @Lob
    @Column(name = "itemDetail", nullable = false)
    private String itemDetail;  // 상품 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStauts itemSellStauts;  // 상품 판매 상태(SELL, SOLD_OUT)

}
