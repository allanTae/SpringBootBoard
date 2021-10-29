package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.infra.AuthenticationConverter;
import com.allan.springBootBoard.security.config.WebSecurityConfig;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.BoardForm;
import com.allan.springBootBoard.web.board.service.BoardService;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@AutoConfigureMybatis
//@AutoConfigureMockMvc
@WithMockUser
public class BoardControllerTest {

    Member TEST_MEMBER;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private AuthenticationConverter authenticationConverter;

    @BeforeEach
    public void setUp(){
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        TEST_MEMBER = createMember();
    }

    @Test
    public void 게시글_목록_조회_테스트() throws Exception {
        //given
        List<BoardDTO> TEST_BOARDS = Arrays.asList(BoardDTO.builder().build());
        given(boardService.findBoardList(any()))
                .willReturn(TEST_BOARDS);

        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);


        //when
        ResultActions resultActions = mvc.perform(get("/board/getBoardList")
                .param("model", "new Model()")
                .param("page", "1")
                .param("range", "1")
                .param("searchType", "title")
                .param("keyword", "test"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("boardList"))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(model().attributeExists("userInfo"))
                .andExpect(view().name("board/boardList"));
    }

    @Test
    public void 게시글_작성_폼_테스트() throws Exception {
        //given, when
        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);

        ResultActions resultActions = mvc.perform(get("/board/boardForm"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("board/boardForm"));
    }

    @Test
    public void 신규_게시글_저장_테스트() throws Exception {
        //given
        BoardForm TEST_BOARD_FORM = BoardForm.builder()
                .boardId(1l)
                .title("TEST_TITLE")
                .content("TEST_CONTENT")
                .tag("TEST")
                .build();

        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);

        //when
        ResultActions resultActions = mvc.perform(post("/board/saveBoard?mode=").with(csrf())
                .flashAttr("boardForm", TEST_BOARD_FORM) // 한번에 객체로 전달하고 싶을 떄 사용.
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        //then
        verify(boardService, atLeastOnce()).save(any(), any(), any());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/getBoardList"))
                .andDo(print());
    }

    @Test
    public void 게시글_수정_테스트() throws Exception {
        //given
        BoardForm TEST_BOARD_FORM = BoardForm.builder()
                .boardId(1l)
                .title("TEST_TITLE")
                .content("TEST_CONTENT")
                .tag("TEST")
                .build();

        //when
        ResultActions resultActions = mvc.perform(post("/board/saveBoard?mode=edit").with(csrf())
                .flashAttr("boardForm", TEST_BOARD_FORM) // 한번에 객체로 전달하고 싶을 떄 사용.
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        //then
        verify(boardService, atLeastOnce()).update(any());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/getBoardList"))
                .andDo(print());
    }

    @Test
    public void 게시글_조회_테스트() throws Exception {
        //given
        BoardDTO TEST_BOARD_DTO = BoardDTO.builder().build();
        given(boardService.findOne(any()))
                .willReturn(TEST_BOARD_DTO);

        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);

        //when
        ResultActions resultActions = mvc.perform(get("/board/boardContent")
                .param("boardId", "1"));

        //then
        resultActions
                .andExpect(model().attributeExists("boardContent"))
                .andExpect(model().attributeExists("replyDTO"))
                .andExpect(model().attributeExists("userInfo"))
                .andExpect(view().name("board/boardContent"));
    }

    @Test
    public void 게시글_삭제_테스트() throws Exception {
        //given, when
        ResultActions resultActions = mvc.perform(get("/board/deleteBoard")
                .param("boardId", "1"));

        //then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/getBoardList"));
    }

    @Test
    public void 게시글_수정_또는_저장시_내용_유효성검사_테스트() throws Exception {
        //given
        // 게시물의 폼 정보에서 title, content 는 필수 요소이다.
        // 현 테스트에서는 title 정보를 포함하지 않은 채 전송했을때를 가정한다.
       BoardForm TEST_BOARD_FORM = BoardForm.builder()
                .boardId(1l)
                .content("TEST_CONTENT")
                .tag("TEST")
                .build();

        //when
        ResultActions resultActions = mvc.perform(post("/board/saveBoard?mode=edit").with(csrf())
                .flashAttr("boardForm", TEST_BOARD_FORM) // 한번에 객체로 전달하고 싶을 떄 사용.
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        //then
        resultActions
                .andExpect(view().name("board/boardForm"));
    }

    @Test
    public void 게시글_수정_폼_테스트() throws Exception {
        //given
        BoardDTO TEST_BOARD_DTO = createBoardDTO();
        given(boardService.findOne((any())))
                .willReturn(TEST_BOARD_DTO);

        //when
        ResultActions resultActions = mvc.perform(get("/board/editForm")
                .param("boardId", "1")
                .param("mode", "edit"));

        //then
        resultActions
                .andExpect(model().attributeExists("boardForm"))
                .andExpect(model().attributeExists("mode"))
                .andExpect(view().name("board/boardForm"));
    }

    private BoardDTO createBoardDTO() {
        BoardDTO boardDTO = BoardDTO.builder()
                .title("TEST_TITLE")
                .content("TEST_CONTENT")
                .tag("TEST_TAG")
                .build();

        ReflectionTestUtils.setField(boardDTO, "boardId", 1l);

        return boardDTO;
    }

    private Member createMember() {
        Member member = Member.builder()
                .name("test")
                .authId("tester")
                .role(MemberRole.USER)
                .build();
        return member;
    }
}
