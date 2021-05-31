package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class RestMemberController {

    @NonNull MemberRepository memberRepository;

    @PostMapping("/checkId")
    public CheckId checkDuplicateId(@RequestParam(value = "memberId") String memberId){

        String ID_PATTERN = "^[a-zA-Z0-9]{10,16}$";
        if(!Pattern.compile(ID_PATTERN).matcher(memberId).find()){
            return new CheckId(CheckId.INVALID, "아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다.");
        }

        Member member = memberRepository.findOneById(memberId);
        if(member != null) {
            log.error(memberId + " is already in use.");
            return new CheckId(CheckId.IN_USE, "이미 사용중인 아이디 입니다.");
        }else{
            return new CheckId(CheckId.IN_NOT_USE, "사용 가능한 아이디 입니다.");
        }
    }

    @Getter
    @Setter
    private class CheckId{

        public static final String IN_USE = "in use";
        public static final String IN_NOT_USE = "in not use";
        public static final String INVALID = "invalid";

        private CheckId(String status, String message){
            this.status = status;
            this.message = message;
        }

        private String status;
        private String message;
    }
}
