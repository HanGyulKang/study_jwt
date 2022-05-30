package com.study.jwt.filter;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

// Filter를 구현하면 얘가 필터가 됨
public class MyFilter1 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터-1");

        // 다시 필터 타게끔 설정
        // PrintWriter writer = response.getWriter();
        // writer.println("안녕");
        // 이래버리면 프로그램이 여기서 그냥 끝남
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
