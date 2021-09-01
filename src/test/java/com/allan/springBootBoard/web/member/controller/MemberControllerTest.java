package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.security.config.WebSecurityConfig;
import com.allan.springBootBoard.web.config.WebConfig;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@Import(WebConfig.class)
@AutoConfigureMybatis
@WithMockUser
public class MemberControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mvc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    MemberService memberService;

    @BeforeEach
    private void setUp(){
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void 회원_가입성공_테스트() throws Exception {
        //given
        MemberForm TEST_MEMBER_FORM = new MemberForm();
        TEST_MEMBER_FORM.setAuthId("testerId1234");
        TEST_MEMBER_FORM.setName("테스터");
        TEST_MEMBER_FORM.setPwd("qwer1234!!");
        TEST_MEMBER_FORM.setRe_pwd("qwer1234!!");
        TEST_MEMBER_FORM.setGender("2"); // Gender 객체의 id 값이 전달됨.
        TEST_MEMBER_FORM.setYear("1992");
        TEST_MEMBER_FORM.setMonth("03");
        TEST_MEMBER_FORM.setDay("23");
        TEST_MEMBER_FORM.setJibunAddress("TEST_JIBUN_ADDRESS");
        TEST_MEMBER_FORM.setRoadAddress("TEST_ROAD_ADDRESS");
        TEST_MEMBER_FORM.setExtraAddress("TEST_EXTRA_ADDRESS");
        TEST_MEMBER_FORM.setDetailAddress("TEST_DETAIL_ADDRESS");
        TEST_MEMBER_FORM.setPhone("062-888-3333");
        TEST_MEMBER_FORM.setPostcode("500-2222");

        //when
        ResultActions resultActions = mvc.perform(post("/member").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("memberForm", TEST_MEMBER_FORM));

        //then
        verify(memberService, atLeastOnce()).join(any(), any());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/getBoardList"));
    }

    @Test
    public void 회원가입_폼_테스트() throws Exception {
        //given, when
        ResultActions resultActions = mvc.perform(get("/member/signupForm"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("memberForm"))
                .andExpect(model().attributeExists("genders"))
                .andExpect(view().name("member/signupForm"));
    }

    @Test
    public void 회원가입_실패_테스트() throws Exception {
        //given
        MemberForm TEST_MEMBER_FORM = new MemberForm();
        TEST_MEMBER_FORM.setAuthId("test12");
        TEST_MEMBER_FORM.setName("tester");
        TEST_MEMBER_FORM.setPwd("qwer1234");
        TEST_MEMBER_FORM.setRe_pwd("qwer123");
        TEST_MEMBER_FORM.setYear("19923");
        TEST_MEMBER_FORM.setMonth("03");
        TEST_MEMBER_FORM.setDay("23");

        //when
        ResultActions resultActions = mvc.perform(post("/member").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("memberForm", TEST_MEMBER_FORM));

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(view().name("member/signupForm"));
    }
}
