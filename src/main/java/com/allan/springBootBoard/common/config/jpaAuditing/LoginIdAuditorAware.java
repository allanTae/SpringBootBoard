package com.allan.springBootBoard.common.config.jpaAuditing;

import com.allan.springBootBoard.security.user.domain.UserDetailsVO;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class LoginIdAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null == authentication || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            return null;
        }
        UserDetailsVO user = (UserDetailsVO) authentication.getPrincipal();
        return Optional.of(user.getUsername());
    }
}
