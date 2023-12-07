package com.study.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}") // 프로퍼티 파일에 설정한 uploadPath 값 읽어옴
    String uploadPath;  // file:///C:/Study/Project_documents/shop_documents/

    // 외부 경로 리소스를 url로 바로 불러올 수 있도록 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹 브라우저에 입력하는 url에 /images/~~ 로 시작하면 uploadPath에 설정한 폴더 기준으로 파일 읽어옴
        registry.addResourceHandler("/images/**")
                // 로컬 컴퓨터에 저장된 파일 읽어올 root 경로
                .addResourceLocations(uploadPath);
    }
}
