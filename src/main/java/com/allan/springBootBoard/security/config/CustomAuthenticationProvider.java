package com.allan.springBootBoard.security.config;

import com.allan.springBootBoard.security.user.domain.UserDetailsVO;
import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource(name="userDetailsService")
    private UserDetailsService userDetailsService;

    @NonNull
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        String userId = token.getName();
        String userPwd = (String) token.getCredentials();

        // UserDetailsService를 통해 DB에서 아이디로 사용자 조회
        UserDetailsVO userDetailsVO = (UserDetailsVO) userDetailsService.loadUserByUsername(userId);
        if(!bCryptPasswordEncoder.matches(userPwd, userDetailsVO.getPassword())){
            throw new BadCredentialsException("BadCredentialsException");
        }
        return new UsernamePasswordAuthenticationToken(userDetailsVO, userPwd, userDetailsVO.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
