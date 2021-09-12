package com.allan.springBootBoard.security.user.domain;

import com.allan.springBootBoard.web.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
public class UserDetailsVO implements UserDetails {

    @Delegate
    private Member member;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return member.getAuthId();
    }

    @Override
    public String getPassword() {
        return member.getPwd();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.getIsEnable();
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getIsEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return member.getIsEnable();
    }

    @Override
    public boolean isEnabled() {
        return member.getIsEnable();
    }
}
