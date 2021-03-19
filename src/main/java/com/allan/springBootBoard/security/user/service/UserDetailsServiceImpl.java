package com.allan.springBootBoard.security.user.service;

import com.allan.springBootBoard.security.user.domain.UserDetailsVO;
import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import com.allan.springBootBoard.web.exception.NoDataException;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service("userDetailsService")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id){
        try {
            return memberRepository.findByMemberId(id).map(u -> new UserDetailsVO(u, Collections
                    .singleton(new SimpleGrantedAuthority(u.getRole().getValue())))).orElseThrow(() -> new UserNotFoundException("UserNotFoundException"));
        } catch(EmptyResultDataAccessException ex){
            throw new UserNotFoundException("UserNotFoundException");
        }
    }
}
