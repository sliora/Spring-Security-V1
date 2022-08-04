package com.cos.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  //manager로 오게 되면 Role이 필요
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")                               //admin으로 접속하면 Role이 필요함
                .anyRequest().permitAll()                                                                                   //그 외 다른 주소는 허용
                .and()
                .formLogin()                                                                                                //formLogin 방식 사용
                .loginPage("/login");                                                                                       //antMatchers 적용된 경로 이동시 loginPage로 이동
    }
}
