package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity //시큐리티 활성화 하기 위해
public class SecurityConfig {

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
                .loginPage("/login")  //권한 없는 페이지에 접근 했을때 이 주소로 가도록 redirect
                .and().build(); //끝에 꼭 이거 추가
    }
}
