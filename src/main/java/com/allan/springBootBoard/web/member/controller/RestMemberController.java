package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import com.allan.springBootBoard.web.exception.SameIdUseException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import com.allan.springBootBoard.web.member.service.MemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restUser")
@RequiredArgsConstructor
@Slf4j
public class RestMemberController {

    @NonNull MemberRepository memberRepository;

    @PostMapping("/checkId")
    public String checkDuplicateId(@RequestBody MemberForm form){
        String result = "";
        Member member = memberRepository.findOneById(form.getMemberId());
        if(member != null) {
            result = "in use";
            log.error(form.getMemberId() + " is already in use.");
        }else{
            result = "in not use";
        }
        return result;
    }
}
