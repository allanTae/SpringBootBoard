package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.common.Pagination;
import com.allan.springBootBoard.infra.AuthenticationConverter;
import com.allan.springBootBoard.security.config.WebSecurityConfig;
import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.board.domain.model.MyBoardDTO;
import com.allan.springBootBoard.web.board.service.MyBoardService;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers = MyBoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@AutoConfigureMybatis
@AutoConfigureMockMvc
@WithMockUser
public class MyBoardControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MyBoardService myBoardService;

    @MockBean
    AuthenticationConverter authenticationConverter;

    @Test
    public void 자신이_작성한_게시글_목록_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember();
        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);

        int TEST_MY_BOARD_LIST_CNT = 1;
        given(myBoardService.getMyBoardListCnt(any()))
                .willReturn(TEST_MY_BOARD_LIST_CNT);

        List<MyBoardDTO> myBoardDTOList = createMyBoardDTOList();
        given(myBoardService.getMyBoardList(any(), any(Pagination.class)))
                .willReturn(myBoardDTOList);

        //when
        ResultActions resultActions = mvc.perform(get("/myBoard/getBoardList")
            .param("page", "1")
            .param("range", "1"));


        //then
        resultActions
                .andExpect(model().attributeExists("boardList"))
                .andExpect(view().name("board/myBoardList"));
    }

    private List<MyBoardDTO> createMyBoardDTOList() {
        return List.of(
                new MyBoardDTO(1l, "test", 1l, "tester", "2020-09-16 23:44:15"),
                new MyBoardDTO(2l, "test", 2l, "tester", "2020-09-16 23:44:15")
        );
    }

    private Member createMember() {
        return Member.builder()
                .name("test")
                .address(new Address("test", "test", "test", "test", "test"))
                .gender(Gender.MAN)
                .age(10l)
                .authId("testId")
                .pwd("testPwd")
                .build();
    }
}
