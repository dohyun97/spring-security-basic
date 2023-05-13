package com.cos.security1.config;

import com.cos.security1.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity //시큐리티 활성화 하기 위해
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()   //이 URL은 로그인한 회원만 접근 가능
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") //로그인 및 롤이 admin/manager인 회원만
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //로그인 하고 Admin 롤 인 회원만
                .anyRequest().permitAll() //위 URL이 아닌 주소는 아무나 접근 가능
                .and()
                .formLogin()
                .loginPage("/loginForm")  //권한 없는 페이지에 접근 했을때 이 주소로 가도록 redirect
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login() //oauth2 login
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService) //구글 로그인 완료된 뒤의 후처리가 필요. 이렇게 하면 로그인 후 코드(인증)를 받는게 아니라 토큰(권한)&사용자 프로필을 받아
                .and()
                .and().build(); //끝에 꼭 이거 추가
    }
    //여기에 Bcrypt 빈을 생성했더니 순환참조 문제 발생. principalOauth2UserService는 securityConfig에 있는 Bcrypt 를 주입 받고
    // securityConfig는 principalOauth2UserService에 있는 PrincipalOauth2UserService를 주입 받아서
}
