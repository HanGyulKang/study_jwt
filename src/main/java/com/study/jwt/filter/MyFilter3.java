package com.study.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// Filter를 구현하면 얘가 필터가 됨
public class MyFilter3 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 : 코스
        // 검증은 시큐리티 시작 전에 반드시 진행 되어야 함
        /** [ 검증 설계 ]
         * 토큰 : cos 이걸 만들워줘야 함. id, pw가 정상적으로 들어와서 로그인이 완료 되면 토큰을 만들어주고 그걸 응답해준다.
         * 요청할 때 마다 header의 Authorization에 value값으로 토큰을 가지고 오겠죠?
         * 그 때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증하면 됨.(RSA, HS256)
         */
        if("POST".equals(req.getMethod())) {
            System.out.println("POST 요청 됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("필터-3");

            System.out.println(headerAuth);
            if("cos".equals(headerAuth)) {
                chain.doFilter(req, res);
            } else {
                PrintWriter outPrintWriter = res.getWriter();
                outPrintWriter.println("인증 안 됨");
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
