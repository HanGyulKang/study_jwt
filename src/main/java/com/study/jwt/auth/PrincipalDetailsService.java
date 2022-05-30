package com.study.jwt.auth;

import com.study.jwt.entity.User;
import com.study.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login 시 동작
// 스프링 로그인 기본 주소가 /login
// 하지만 JWT 기본 설정(SecurityConfig) 때문에 /login이 동작을 안 함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(this.getClass().getName() + "의 loadUserByUsername 메서드");
        User userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
