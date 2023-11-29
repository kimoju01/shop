package com.study.shop.entity;

import com.study.shop.constant.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order 키워드랑 겹치지않게 orders로 설정
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Orders extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 한 명의 회원은 여러 번의 주문을 할 수 있다
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "order_date")
    private LocalDateTime orderDate;    // 주문한 날짜

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문 상태

    // mappedBy? OrderItem 엔티티와 양방향 매핑을 함
    // 외래 키가 있는 OrderItem 엔티티의 orders 필드가 관계의 주인이 됨
    // 주인이 아닌 엔티티에서는 mappedBy로 주인 엔티티의 필드와 매핑되는 것을 나타냄
    // CascadeType.ALL? 부모 엔티티 영속성 상태 변화를 자식 엔티티에 모두 전이함
    // => ex) Orders 엔티티를 저장하면 OrderItem 엔티티도 함께 저장.. 이런 변화
    // orphanRemoval? 부모 엔티티와 연관 관계가 끊어진 자식 엔티티(고아 객체) 제거
    // => ex) Orders 엔티티에서 OrderItem 엔티티 삭제했을 때 OrderItem 엔티티가 삭제 됨.
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)  // 하나의 주문은 여러 개의 주문 상품을 가질 수 있다
    private List<OrderItem> orderItems = new ArrayList<>();

}
