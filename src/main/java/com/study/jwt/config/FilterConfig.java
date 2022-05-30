package com.study.jwt.config;

import com.study.jwt.filter.MyFilter1;
import com.study.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> myFilter1Bean = new FilterRegistrationBean<>(new MyFilter1());
        // 모든 url에서 다 돌아라
        myFilter1Bean.addUrlPatterns("/*");
        // 낮은 번호가 필터중에 가장 먼저 실행 됨
        myFilter1Bean.setOrder(1);
        return myFilter1Bean;
    }


    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> myFilter2Bean = new FilterRegistrationBean<>(new MyFilter2());
        // 모든 url에서 다 돌아라
        myFilter2Bean.addUrlPatterns("/*");
        // 낮은 번호가 필터중에 가장 먼저 실행 됨
        myFilter2Bean.setOrder(0);
        return myFilter2Bean;
    }
}
