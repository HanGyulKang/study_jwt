package com.study.jwt.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요청 시 username, password를 전송하면
// UsernamePasswordAuthenticationFilter 가 동작을 함
// SecurityConfig에 .formLogin().disable() 설정이 되어있어서 작동을 안 하기 때문에
// SecurityConfig에 이 필터를 등록해줘야 함

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // 로그인 시 동작(/login) : 로그인 요청 시 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println(this.getClass().getName() + " attemptAuthentication : 로그인 시도중");

        // 1. username, password를 받는다.
        // 2. 정상인지 로그인 시도를 한다.
        // 3. authenticationManager 객체로 로그인 시도를 하면 PrincipalDetailsService가 실행 됨
        // 4. PrincipalDetailsService 클래스의 loadUserByUsername 메서드가 자동으로 실행 됨
        // 5. PrincipalDetails를 세션에 담는다. (세션에 안 담으면 권한 관리가 안 됨 : 권한관리 안 할거면 안 담아도 됨)
        // 6. JWT 토큰을 만들어서 응답해주면 됨
        return super.attemptAuthentication(request, response);
    }
}
