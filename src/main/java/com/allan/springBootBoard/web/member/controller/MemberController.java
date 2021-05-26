package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.service.MemberService;
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
import java.util.List;
import java.util.regex.Pattern;

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

        boolean validationResult = true;

        // 유효성 검사
        if(!Pattern.compile(ID_PATTERN).matcher(form.getMemberId()).find()){
            bindingResult.rejectValue("memberId", "id.invalidatedVal", "아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(NAME_PATTERN).matcher(form.getName()).find()){
            bindingResult.rejectValue("name", "name.invalidatedVal", "이름은 한글 2자~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(PASSWORD_PATTERN).matcher(form.getPwd()).find()){
            bindingResult.rejectValue("pwd", "pwd.invalidatedVal", "비밀번호는 영대소문자, 숫자, 특수문자를 반드시 한글자 포함하고, 10~16자까지만 입력이 가능합니다.");
        }

        if(!bindingResult.hasErrors() && !form.getPwd().equals(form.getRe_pwd())){
            bindingResult.rejectValue("pwd", "pwd.invalidatedVal", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            bindingResult.rejectValue("re_pwd", "re_pwd.invalidatedVal", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }


        if(bindingResult.hasErrors()){
            return "member/signupForm";
        }

        Member member = Member.builder()
                .id(form.getMemberId())
                .pwd(passwordEncoder.encode(form.getPwd()))
                .role(MemberRole.USER)
                .name(form.getName())
                .address(new Address(form.getCity(), form.getStreet(), form.getZipcode()))
                .age(form.getAge())
                .phoneNumber(form.getPhone())
                .gender(Gender.valueOf(Integer.parseInt(form.getGender()))) // 폼에서 전달 된 String 값을 int로 변환하기 위함.
                .createdBy(form.getMemberId())
                .createdDate(LocalDateTime.now())
                .build();

        memberService.join(member);
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
