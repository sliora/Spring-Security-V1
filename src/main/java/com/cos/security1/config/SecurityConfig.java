package com.cos.security1.config;

import com.cos.security1.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;



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
                .defaultSuccessUrl("/")                                                                                    //logout을 하면 /로 가는데, 특정 페이지가 있으면 거기로 바로 연결해줌
            .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);


        //**구글 로그인이 완료되면 코드가 만들어지는게 아님, 액세스 토큰 + 사용자프로필정보 받음

        //1.코드받기(인증) 2. 액세스토큰(권한)
        //3. 사용자프로필 정보 가져옴 4. 그 정보를 토대로 회원가입 자동 진행 등
        //4.2 (이메일, 전화번호, 이름, 아이디) -> 집주소 등 추가 정보 입력


    }
}
