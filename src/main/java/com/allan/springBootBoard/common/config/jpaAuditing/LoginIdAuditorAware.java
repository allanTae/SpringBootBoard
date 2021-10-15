package com.allan.springBootBoard.common.config.jpaAuditing;

import com.allan.springBootBoard.security.user.domain.UserDetailsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;

public class LoginIdAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증이 되지 않은 회원이거나 알수 없는 회원인 경우
        if(null == authentication || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            return null;
        }

        // 인증 된 회원이 일반 서비스 인증이냐, oauth 인증이냐를 구분하여 Principal 반환.
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UserDetailsVO user = (UserDetailsVO) authentication.getPrincipal();
            return Optional.of(user.getUsername());
        }else if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            String authId = (String) user.getAttribute("authId");
            return Optional.of(authId);
        }
        return null;
    }
}
