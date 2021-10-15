package com.allan.springBootBoard.security.user.test;

import com.allan.springBootBoard.security.user.domain.UserDetailsVO;
import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member TestMember = Member.builder()
                .authId(customUser.userId())
                .name(customUser.userName())
                .pwd("testPwd")
                .role(MemberRole.USER)
                .build();

        UserDetailsVO principal = new UserDetailsVO(TestMember, Collections
                .singleton(new SimpleGrantedAuthority(TestMember.getRole().getKey())));
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}