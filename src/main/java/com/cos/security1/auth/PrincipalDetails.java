package com.cos.security1.auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다. (Security ContextHolder)
//오브젝트


//Security Session 에는 Authentication 객체만 들어갈 수 있음
//Authentication 객체에 UserDetail(PrincipalDetails) 객체로 접근이 가능함?

import com.cos.security1.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole());

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        //만료여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //잠금여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //기간만료
        return true;
    }

    @Override
    public boolean isEnabled() {
        //사이트에서 1년동안 회원이 로그인을 안하면 휴면 계정으로 할 때 사용
        //유저 모델에 loginDate를 넣어두고 현재시간 - 로긴시간 = 초과되면 return false로 설정하면 됨.

        return true;
    }
}
