package com.study.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 지연(LAZY) 로딩? 일대일, 다대일 매핑 시 기본 전략인 즉시 로딩은 OrderItem 엔티티를 가져올 경우
    // Item, Orders, Member까지 4개의 테이블을 조인해서 가져온다. => 비즈니스 로직에서 필요 없는 데이터도 가져온다
    // => 매핑 되는 엔티티의 개수가 많아지면 성능 문제, 쿼리 예측 문제 생김 => 지연 로딩 방식 이용하는게 좋음
    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 단방향 매핑. 하나의 상품은 여러 주문 상품으로 들어갈 수 있다.
    @JoinColumn(name = "item_id")
    private Item item;

    // Orders 엔티티와 양방향 매핑
    // 관계의 주인은 외래 키를 가지고 있는 OrderItem 엔티티임 (Orders에 mappedBy로 관계의 주인 나타냄)
    @ManyToOne(fetch = FetchType.LAZY)  // 한 번의 주문에 여러 개의 상품을 주문할 수 있다.
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Column(name = "price")
    private int price;  // 주문 가격

    @Column(name = "count")
    private int count;  // 수량

}
