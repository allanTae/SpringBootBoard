package com.allan.springBootBoard.infra;

import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationConverter {

    @NonNull
    MemberRepository memberRepository;

    public Member getMemberFromAuthentication(Authentication authentication){
        String authId = "";
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            authId = authentication.getName();
        }else if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            authId = (String) user.getAttribute("authId");
        }

        return memberRepository.findByAuthId(authId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
    }
}
