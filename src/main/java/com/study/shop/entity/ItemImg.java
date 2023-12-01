package com.study.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;    // 이미지 파일명

    @Column(name = "origin_name")
    private String originName;  // 원래 이미지 파일명

    @Column(name = "url")
    private String url;  // 이미지 조회 경로

    @Column(name = "rep_img")
    private String repImg;    // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 단방향 매핑. 하나의 상품에는 여러 개의 이미지가 존재할 수 있다.
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String originName, String name, String url) {
        // 이미지 정보 업데이트
        this.originName = originName;
        this.name = name;
        this.url = url;
    }

}
