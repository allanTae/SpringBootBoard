package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.web.error.exception.MemberNotFoundException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import com.allan.springBootBoard.web.member.service.MemberService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/member")
@Slf4j
public class RestMemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/checkId")
    public ResponseEntity<CheckId> checkDuplicateId(@RequestParam(value = "authId") String authId){

        String ID_PATTERN = "^[a-zA-Z0-9]{10,16}$";
        CheckId checkId = null;
        if(!Pattern.compile(ID_PATTERN).matcher(authId).find()){
                checkId = new CheckId(CheckId.INVALID, "아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다.");
            return new ResponseEntity<CheckId>(checkId, HttpStatus.BAD_REQUEST);
        }
        Member member;
        try{
            member = memberService.findByAuthId(authId);
        }catch (MemberNotFoundException e){
            checkId = new CheckId(CheckId.IN_NOT_USE, "사용 가능한 아이디 입니다.");
            return new ResponseEntity<CheckId>(checkId, HttpStatus.OK);
        }
        log.error(authId + " is already in use.");
        checkId = new CheckId(CheckId.IN_USE, "이미 사용중인 아이디 입니다.");
        return new ResponseEntity<CheckId>(checkId, HttpStatus.OK);
    }

    @Getter
    @Setter
    private class CheckId{

        public static final String IN_USE = "in use";
        public static final String IN_NOT_USE = "in not use";
        public static final String INVALID = "inputInvalidException";

        private CheckId(String status, String message){
            this.status = status;
            this.message = message;
        }

        private String status;
        private String message;
    }
}
