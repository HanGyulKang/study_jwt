package com.study.jwt.config;

import com.study.jwt.filter.MyFilter1;
import com.study.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Filter
        // BasicAuthenticationFilter 얘가 스프링 시큐리티 필터 전에 실행되기 때문에 쟤를 넣어 줌...
        // 스프링 시큐리티 필터가 아니라서 스프링 시큐리티 필터가 돌기 전에 만든 필터를 호출해야 함
        // 생으로 만든 필터는 스프링 시큐리티 필터보다 뒤에 실행 됨
        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);

        http.csrf().disable();

        // 23 line ~ 30 line 까지는 JWT 서버 셋팅에 필수
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 x
                .and()
                /**
                 * [ filter ]
                 * 모든 요청은 해당 필터를 탐 (CORS[Cross Origin] 정책에서 벗어날 수 있음 : 크로스 오버 요청이 들어오면 모두 허용)
                 */
                .addFilter(corsFilter)
                .formLogin().disable() // form 태그를 만들어서 Login하는 방식은 하지 않겠다.
                /**
                 * [ httpBasic().disable() 이란? 요거 한 번 이해해보자. ]
                 * 예 : 로그인 시 id, pw를 서버로 보냄. 그냥 보내면 외부에 노출이 되기 때문에 https를 써야 함 -> 이게 httpBasic
                 *
                 * 하지만(but)?
                 * JWT를 쓰면 id, pw가 아닌 토큰을 보냄
                 * 토큰은 노출 되면 안 되지만 비교적 안전함
                 * 토큰 안에 id, pw를 담아서 왔다갔다 함 -> 이게 바로 bearer 방식
                 * 토큰은 유효 시간이라는게 있어서 노출이 되어도 날아 감
                 * 그래서 Http 기본 인증방식을 사용하지 않음(Disable)
                 */
                .httpBasic().disable() // 기본적인 Http 로그인 방식을 사용하지 않겠다(기본 인증 방식).
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                    .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                    .access("hasRole('ROLE_ADMIN')");
    }
}
