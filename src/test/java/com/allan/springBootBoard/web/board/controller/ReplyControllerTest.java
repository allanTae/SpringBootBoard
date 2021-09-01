package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.security.config.WebSecurityConfig;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.service.ReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ReplyController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = WebSecurityConfig.class
        )
)
@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureMybatis
public class ReplyControllerTest {
    String TEST_BOARD_ID = "1";

    @MockBean
    ReplyService replyService;

    @Autowired
    MockMvc mvc;

    @Test
    public void 댓글_목록_테스트() throws Exception {
        //given
        ReplyDTO TEST_REPLY_1 = ReplyDTO.builder()
                .registerId("TESTER_1")
                .content("TEST_CONTENT_1")
                .build();

        ReplyDTO TEST_REPLY_2 = ReplyDTO.builder()
                .registerId("TESTER_2")
                .content("TEST_CONTENT_2")
                .build();

        List<ReplyDTO> TEST_REPLY_DTOES = List.of(TEST_REPLY_1, TEST_REPLY_2);
        given(replyService.list(any()))
                .willReturn(TEST_REPLY_DTOES);

        //when
        ResultActions resultActions = mvc.perform(get("/boards/" + TEST_BOARD_ID + "/replies")
                .param("boardId", "2"));

        //then
        verify(replyService,atLeastOnce()).list(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].registerId").value(TEST_REPLY_1.getRegisterId()))
                .andExpect(jsonPath("$[0].content").value(TEST_REPLY_1.getContent()))
                .andExpect(jsonPath("$[1].registerId").value(TEST_REPLY_2.getRegisterId()))
                .andExpect(jsonPath("$[1].content").value(TEST_REPLY_2.getContent()));
    }

    @Test
    public void 게시물_댓글_등록_테스트() throws Exception {
        //given
        Long TEST_SAVE_COUNT = 1l;
        ReplyDTO TEST_REPLY_DTO = new ReplyDTO();
        given(replyService.saveChildReply(any()))
                .willReturn(TEST_SAVE_COUNT);

        //when
        ResultActions resultActions = mvc.perform(post("/boards/" + TEST_BOARD_ID + "/replies/parent-reply").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_REPLY_DTO)));

        //then
        verify(replyService,atLeastOnce()).saveParentReply(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.status").value("200"))
                .andExpect(jsonPath("@.message").value(ReplyController.ReplyResponse.REPLY_ENROLL_SUCCESS));
    }

    @Test
    public void 답글_댓글_등록_테스트() throws Exception {
        //given
        Long TEST_SAVE_COUNT = 1l;
        ReplyDTO TEST_REPLY_DTO = new ReplyDTO();
        TEST_REPLY_DTO.setParentDepth(2l);
        given(replyService.saveChildReply(any()))
                .willReturn(TEST_SAVE_COUNT);

        //when
        ResultActions resultActions = mvc.perform(post("/boards/" + TEST_BOARD_ID + "/replies/child-reply").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_REPLY_DTO)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.status").value("200"))
                .andExpect(jsonPath("@.message").value(ReplyController.ReplyResponse.REPLY_ENROLL_SUCCESS));
    }

    @Test
    public void 답급_최대댓글수_초과_테스트() throws Exception {
        //given
        Long TEST_SAVE_COUNT = 1l;
        ReplyDTO TEST_REPLY_DTO = new ReplyDTO();
        TEST_REPLY_DTO.setParentDepth(5l);
        given(replyService.saveChildReply(any()))
                .willReturn(TEST_SAVE_COUNT);

        //when
        ResultActions resultActions = mvc.perform(post("/boards/" + TEST_BOARD_ID + "/replies/child-reply").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_REPLY_DTO)));

        //then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("@.status").value("403"))
                .andExpect(jsonPath("@.message").value(ReplyController.ReplyResponse.CHILD_REPLY_MAX_COUNT));
    }

    @Test
    public void 댓글_수정_테스트() throws Exception {
        //given
        Long TEST_UPDATE_COUNT = 1l;
        String TEST_REPLY_ID = "1";
        ReplyDTO TEST_REPLY_DTO = new ReplyDTO();
        given(replyService.updateReply(any()))
                .willReturn(TEST_UPDATE_COUNT);

        //when
        ResultActions resultActions = mvc.perform(put("/boards/" + TEST_BOARD_ID + "/replies/" + TEST_REPLY_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_REPLY_DTO)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.status").value("200"))
                .andExpect(jsonPath("@.message").value("댓글 ID: " + TEST_REPLY_ID + " " + ReplyController.ReplyResponse.REPLY_REVICE_SUCCESS));
    }

    @Test
    public void 댓글_삭제_테스트() throws Exception {
        //given
        Long TEST_DELETE_COUNT = 1l;
        String TEST_REPLY_ID = "1";
        ReplyDTO TEST_REPLY_DTO = new ReplyDTO();
        given(replyService.deleteReply(any()))
                .willReturn(TEST_DELETE_COUNT);

        //when
        ResultActions resultActions = mvc.perform(delete("/boards/" + TEST_BOARD_ID + "/replies/" + TEST_REPLY_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_REPLY_DTO)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.status").value("200"))
                .andExpect(jsonPath("@.message").value("댓글 ID: " + TEST_REPLY_ID + " " + ReplyController.ReplyResponse.REPLY_REMOVE_SUCCESS));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
