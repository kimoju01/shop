package com.study.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItem extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 단방향 매핑. 하나의 장바구니에는 여러 개의 상품을 담을 수 있다
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 단방향 매핑. 하나의 상품은 여러 개의 장바구니 상품으로 담길 수 있다
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "count")
    private int count;  // 같은 상품을 장바구니에 몇 개 담을지

}
