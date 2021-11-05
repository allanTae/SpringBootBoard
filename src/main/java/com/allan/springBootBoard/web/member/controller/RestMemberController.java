package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.web.error.exception.MemberNotFoundException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

    /**
     * 회원가입시, 아이디 중복 체크를 위한 메소드입니다.
     * @param authId
     * @return
     */
    @PostMapping("/checkId")
    public ResponseEntity<CheckResponse> checkDuplicateId(@RequestParam(value = "authId") String authId){

        String ID_PATTERN = "^[a-zA-Z0-9]{10,16}$";
        CheckResponse checkId = null;
        if(!Pattern.compile(ID_PATTERN).matcher(authId).find()){
                checkId = new CheckResponse(CheckResponse.INVALID, "아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다.");
            return new ResponseEntity<CheckResponse>(checkId, HttpStatus.BAD_REQUEST);
        }
        Member member;
        try{
            member = memberService.findByAuthId(authId);
        }catch (MemberNotFoundException e){
            checkId = new CheckResponse(CheckResponse.IN_NOT_USE, "사용 가능한 아이디 입니다.");
            return new ResponseEntity<CheckResponse>(checkId, HttpStatus.OK);
        }
        log.error(authId + " is already in use.");
        checkId = new CheckResponse(CheckResponse.IN_USE, "이미 사용중인 아이디 입니다.");
        return new ResponseEntity<CheckResponse>(checkId, HttpStatus.OK);
    }

    /**
     * 비밀번호 찾기 기능에서 입력받은 회원이름과 회원 이메일 정보로 회원가입 유무를 확인하는 메소드입니다.
     * @param memberName
     * @param authId
     * @return
     */
    @GetMapping("/check/findPwd")
    public ResponseEntity<CheckResponse> findPwd(@RequestParam(value = "memberName") String memberName,@RequestParam(value = "authId") String authId){
        CheckResponse checkPwd = null;
        boolean isPwd = false;
        try{
            isPwd = memberService.isJoined(memberName, authId);
        }catch(MemberNotFoundException e){
            checkPwd = new CheckResponse(CheckResponse.IN_NOT_USE, "존재하지 않는 회원정보입니다.");
            log.info("result: " + checkPwd.getMessage());
            return new ResponseEntity<CheckResponse>(checkPwd, HttpStatus.OK);
        }

        // 프론트단에서 입력받은 아이디와 이름의 동일 계정 여부 확인.
        if(isPwd){
            checkPwd = new CheckResponse(CheckResponse.IN_USE, "회원이 존재합니다.");
            log.info("result: " + checkPwd.getMessage());
            return new ResponseEntity<>(checkPwd, HttpStatus.OK);
        }else{
            checkPwd = new CheckResponse(CheckResponse.INVALID, "입력하신 회원정보를 다시 확인 해 주세요.");
            log.info("result: " + checkPwd.getMessage());
            return new ResponseEntity<>(checkPwd, HttpStatus.OK);
        }

    }

    /**
     * @param memberName
     * @param authId
     * @return
     */
    @PostMapping("/check/findPwd/sendEmail")
    public ResponseEntity<ApiResponse> changePwd(@RequestParam(value = "memberName") String memberName, @RequestParam(value = "authId") String authId){
        log.info("changePwd() call!!!");
        try{
            memberService.changePwdAndSendEmail(memberName,authId);
        }catch (RuntimeException e){
            log.error("메시지 발송에 실패하였습니다.");
            ApiResponse apiResponse = new ApiResponse(ApiResponse.FAIL);
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ApiResponse apiResponse = new ApiResponse(ApiResponse.SUCCESS);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class CheckResponse {

        // 프론트단에서 사용 할 확인 결과 정보
        public static final String IN_USE = "in use";
        public static final String IN_NOT_USE = "in not use";
        public static final String INVALID = "inputInvalidException";

        private String status;
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class ApiResponse {
        public static final String SUCCESS = "success";
        public static final String FAIL = "fail";

        public String status;
    }
}
