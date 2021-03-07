package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.service.MemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/loginForm")
    public String loginView(){
        return "/user/loginForm";
    }

    /**
     * 일반 회원 등록.
     * @param form
     * @return
     */
    @PostMapping("/register/user")
    public String joinUser(@ModelAttribute("memberForm") @Valid MemberForm form, BindingResult bindingResult){

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

    @GetMapping("/signupForm")
    public String signupForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "user/signupForm";
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
