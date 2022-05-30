package com.study.jwt.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.jwt.auth.PrincipalDetails;
import com.study.jwt.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

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
        System.out.println("======================= 로그인 시도 =======================");
        try {
            // x-www-form-urlencoded
            /**
            BufferedReader br = request.getReader();

            String input = null;
            while((input = br.readLine()) != null) {
                System.out.print(input);
            }

            System.out.println();
            System.out.println(request.getInputStream().toString());
            */

            // JSON (body)
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            System.out.println(user);

            // Test용 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            System.out.println("authenticationToken : " + authenticationToken);

            // 로그인 시도
            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행 됨
            // 함수 실행 이후 정상이면 authentication이 return 됨
            // password는 스프링이 처리 함... ㅠㅠㅠㅠㅠ username만 있으면 됨
            // DB에 있는 username과 password 일치 여부를 판단.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 데이터 확인
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            // 출력이 되는 데이터가 있다면 로그인이 되었다는 것
            System.out.println("principalDetails.getUser().getUsername() : " + principalDetails.getUser().getUsername());
            System.out.println("principalDetails.getUser().getPassword() : " + principalDetails.getUser().getPassword());

            System.out.println("======================= 로그인 성공 =======================");

            // return 될 때 authentication 객체가 session 영역에 저장 됨
            // 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임
            // 굳이 JWT 토큰을 사용하면서 session을 만들 이유가 없으나 단지 권한처리때문에 session에 넣어주는거임
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 2. 정상인지 로그인 시도를 한다.
        // 3. authenticationManager 객체로 로그인 시도를 하면 PrincipalDetailsService가 실행 됨
        // 4. PrincipalDetailsService 클래스의 loadUserByUsername 메서드가 자동으로 실행 됨
        // 5. PrincipalDetails를 세션에 담는다. (세션에 안 담으면 권한 관리가 안 됨 : 권한관리 안 할거면 안 담아도 됨)
        // 6. JWT 토큰을 만들어서 응답해주면 됨
        // return super.attemptAuthentication(request, response);
    }

    /**
     * attemptAuthentication() 실행 이후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행 됨.
     * JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 reponse 해주면 됨
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행 : 인증 완료");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
