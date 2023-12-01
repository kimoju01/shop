package com.study.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   // @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImgDto {

    private Long id;

    private String name;    // 이미지 파일명

    private String originName;  // 원래 이미지 파일명

    private String url;  // 이미지 조회 경로

    private String repImg;    // 대표 이미지 여부

}
