package com.study.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 내 서버가 응답을 할 때 JSON을 자바스크립트에서 처리할 수 있게 할지를 설정
        // false가 되어있다면 자바스크립트로 요청이 들어왔을 때 응답을 안 함
        corsConfiguration.setAllowCredentials(true);
        // 모든 ip에 응답 허용
        corsConfiguration.addAllowedOrigin("*");
        // 모든 header에 응답 허용
        corsConfiguration.addAllowedHeader("*");
        // 모든 method(post, get, put, delete, patch 등의 요청을 허용
        corsConfiguration.addAllowedMethod("*");

        // /api/** 이하를 타는 모든 api는 해당 설정을 적용받도록 함
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
