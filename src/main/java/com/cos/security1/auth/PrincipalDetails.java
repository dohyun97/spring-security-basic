package com.cos.security1.auth;

import com.cos.security1.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//security session(securityContextHolder) => Authentication => UserDetails(PrincipalDetails) = principal
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user;
    private Map<String,Object> attributes;

    //일반 로그인시 사용
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //Oauth 로그인시 사용 - oauth로그인 시 principalDetails가 attribute 정보를 가지고 있어. User정보는 attribute를 토대로 만들꺼야
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //해당 유저의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //권한은 user.getRole() 하면 되는데 리턴 타입을 맞춰줘야 되서
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //Oauth2User override
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;  //Oauth 로그인시 생성자를 통해서 map<String, object> attributes 정보 담아줘
    }
   //이건 사용 안해서 그냥 null
    @Override
    public String getName() {
        return null;
    }
}
