package com.study.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Fetch 전략 기본 설정 값 -> 즉시 로딩 (EAGER)
    @OneToOne(fetch = FetchType.LAZY)   // 일대일 단방향 매핑. 한 명의 회원은 하나의 장바구니를 갖는다.
    @JoinColumn(name = "member_id")
    private Member member;

}
