package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()                                                        //authenticated 인증만 되면 들어갈 수 있음
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  //manager로 오게 되면 Role이 필요
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")                               //admin으로 접속하면 Role이 필요함
                .anyRequest().permitAll()                                                                                   //그 외 다른 주소는 허용
                .and()
                .formLogin()                                                                                                //formLogin 방식 사용
                .loginPage("/loginForm")                                                                                    //antMatchers 적용된 경로 이동시 loginPage로 이동
                .loginProcessingUrl("/login")                                                                               //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행함
                .defaultSuccessUrl("/");                                                                                    //logout을 하면 /로 가는데, 특정 페이지가 있으면 거기로 바로 연결해줌

    }
}
