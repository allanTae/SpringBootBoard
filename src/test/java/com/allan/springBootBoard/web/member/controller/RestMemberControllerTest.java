package com.allan.springBootBoard.web.member.controller;

import com.allan.springBootBoard.security.config.WebSecurityConfig;
import com.allan.springBootBoard.web.error.code.ErrorCode;
import com.allan.springBootBoard.web.error.exception.MemberNotFoundException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RestMemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@AutoConfigureMybatis
@WithMockUser
@AutoConfigureMockMvc
public class RestMemberControllerTest {

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mvc;

    @Test
    public void 이미_사용한_아이디_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember();
        given(memberService.findByAuthId(any()))
                .willReturn(TEST_MEMBER);

        //when
        ResultActions resultActions = mvc.perform(post("/member/checkId").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("authId", "testtest1234"));
        //then
        verify(memberService, atLeastOnce()).findByAuthId(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.status").value("in use"))
                .andExpect(jsonPath("@.message").value("이미 사용중인 아이디 입니다."));

    }

    @Test
    public void 신규_아이디_테스트() throws Exception {
        //given
        given(memberService.findByAuthId(any()))
                .willThrow(new MemberNotFoundException("해당 Member 엔티티가 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));

        //when
        ResultActions resultActions = mvc.perform(post("/member/checkId").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("authId", "testtest1234"));

        //then
        verify(memberService, atLeastOnce()).findByAuthId(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.status").value("in not use"))
                .andExpect(jsonPath("@.message").value("사용 가능한 아이디 입니다."));
    }

    @Test
    public void 아이디_유효성검사_테스트() throws Exception {
        //given, when
        ResultActions resultActions = mvc.perform(post("/member/checkId").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("authId", "test1234"));

        //then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("@.status").value("inputInvalidException"))
                .andExpect(jsonPath("@.message").value("아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다."));
    }

    private Member createMember() {
        return Member.builder()
                .build();
    }
}
