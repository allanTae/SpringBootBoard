package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 일반 회원 등록.
     * @param form
     * @return
     */
    @PostMapping("/member")
    public String joinUser(@ModelAttribute("memberForm") @Valid MemberForm form, BindingResult bindingResult){

        String ID_PATTERN = "^[a-zA-Z0-9]{10,16}$";
        String NAME_PATTERN = "^[가-힣]{2,16}$";
        String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*()\\-_=+~₩|\\\\:;\"',.<>/?]{10,16}$";
        String DATE_OF_BIRTH_PATTERN ="^[0-9]{4}-[0-9]{2}-[0-9]{2}$";

        String dateOfBirth = form.getYear() + "-" + form.getMonth() + "-" + form.getDay();

        // 유효성 검사
        if(!Pattern.compile(ID_PATTERN).matcher(form.getMemberId()).find()){
            log.error("memberId: " + form.getMemberId());
            bindingResult.rejectValue("memberId", "id.invalidatedVal", "아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(NAME_PATTERN).matcher(form.getName()).find()){
            log.error("name: " + form.getName());
            bindingResult.rejectValue("name", "name.invalidatedVal", "이름은 한글 2자~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(PASSWORD_PATTERN).matcher(form.getPwd()).find()){
            log.error("pwd: " + form.getPwd());
            bindingResult.rejectValue("pwd", "pwd.invalidatedVal", "비밀번호는 영대소문자, 숫자, 특수문자를 반드시 한글자 포함하고, 10~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(DATE_OF_BIRTH_PATTERN).matcher(dateOfBirth).find()){
            log.error("dateOfBirth: " + dateOfBirth);
            bindingResult.rejectValue("dateOfBirth", "dateOfBirth.invalidatedVal", "생년월일을 제대로 입력해 주세요.");
        }
        if(!bindingResult.hasErrors() && !form.getPwd().equals(form.getRe_pwd())){
            bindingResult.rejectValue("pwd", "pwd.invalidatedVal", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            bindingResult.rejectValue("re_pwd", "re_pwd.invalidatedVal", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        if(bindingResult.hasErrors()){
            return "member/signupForm";
        }

        memberService.join(form, dateOfBirth);
        return "redirect:/board/getBoardList";
    }

    @GetMapping("/member/signupForm")
    public String signupForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "member/signupForm";
    }

    /**
     * memberForm 에 출력 할 radio 버튼 출력 목록.
     * @return
     */
    @ModelAttribute("genders")
    public List<Gender> genders(){
        List<Gender> list = new ArrayList<Gender>();
        list.add(Gender.MAN);
        list.add(Gender.WOMAN);
        return list;
    }

}
