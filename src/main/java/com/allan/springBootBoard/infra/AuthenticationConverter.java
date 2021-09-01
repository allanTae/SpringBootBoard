package com.allan.springBootBoard.infra;

import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationConverter {

    @NonNull
    MemberRepository memberRepository;

    public Member getMemberFromAuthentication(Authentication authentication){
        String authId = authentication.getName();

        return memberRepository.findByAuthId(authId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
    }
}
