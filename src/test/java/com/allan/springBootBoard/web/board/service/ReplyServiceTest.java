package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.ReplyRepository;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
class ReplyServiceTest {

    Long TEST_BOARD_ID = 1l;
    Long TEST_REPLY_ID = 2l;

    ReplyService replyService;

    @Mock
    ReplyRepository replyRepository;

    @Mock
    BoardRepository boardRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    private void setUp(){
        MockitoAnnotations.openMocks(this);
        replyService = new ReplyServiceImpl(replyRepository, boardRepository);

    }

    @Test
    public void 게시글_댓글_저장_테스트() throws Exception {
        //given
        Long TEST_REPLY_ENTITY_ID = 1l;
        Member TEST_MEMBER = createMember();

        Board TEST_BOARD = createBoard(TEST_MEMBER);

        ReplyDTO TEST_REPLY_DTO = createReplyDTO();

        given(boardRepository.findById(any(Long.class)))
                .willReturn(Optional.of(TEST_BOARD));
        given(replyRepository.getMaxReplyId())
                .willReturn(TEST_REPLY_ENTITY_ID);

        //when
        replyService.saveChildReply(TEST_REPLY_DTO);

        //then
        verify(boardRepository, atLeastOnce()).findById(any(Long.class));
        verify(replyRepository, atLeastOnce()).getMaxReplyId();
        verify(replyRepository, atLeastOnce()).save(any(Reply.class));
        assertThat(TEST_BOARD.getReplyList().size(), is(1));
    }


    @Test
    public void 답장_댓글_저장_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember();
        Board TEST_BOARD = createBoard(TEST_MEMBER);
        Long TEST_REPLY_ENTITY_ID = 2l;
        Long TEST_REPLIES_COUNT = 3l;
        Long TEST_REPLIES_UPDATE_GROUP_ORDER = 3l;
        ReplyDTO TEST_REPLY_DTO = createReplyDTO();

        given(boardRepository.findById(any(Long.class)))
                .willReturn(Optional.of(TEST_BOARD));
        given(replyRepository.getMaxReplyId())
                .willReturn(TEST_REPLY_ENTITY_ID);
        given(replyRepository.getReplyListCnt(any(Long.class), any(Long.class)))
                .willReturn(TEST_REPLIES_COUNT);
        given(replyRepository.getGroupOrder(any(Long.class), any(Long.class), any(Long.class), any(Long.class)))
                .willReturn(TEST_REPLIES_UPDATE_GROUP_ORDER);

        //when
        replyService.saveChildReply(TEST_REPLY_DTO);

        //then
        verify(boardRepository, atLeastOnce()).findById(any(Long.class));
        verify(replyRepository, atLeastOnce()).getMaxReplyId();
        verify(replyRepository, atLeastOnce()).getReplyListCnt(any(Long.class), any(Long.class));
        verify(replyRepository, atLeastOnce()).getGroupOrder(any(Long.class), any(Long.class), any(Long.class), any(Long.class));
        verify(replyRepository, atLeastOnce()).updateGroupOrder(any(String.class), any(LocalDateTime.class), any(Long.class), any(Long.class), any(Long.class));
        verify(replyRepository, atLeastOnce()).save(any(Reply.class));
        assertThat(TEST_BOARD.getReplyList().size(), is(1));
    }

    @Test
    public void 댓글_수정_테스트() throws Exception {
        //given
        ReplyDTO TEST_REPLY_DTO = createReplyDTO();
        Reply TEST_REPLY = createReply();
        given(replyRepository.findById(any(Long.class)))
                .willReturn(Optional.of(TEST_REPLY));

        //when
        replyService.updateReply(TEST_REPLY_DTO);

        //then
        verify(replyRepository, atLeastOnce()).findById(any(Long.class));
        assertThat(TEST_REPLY.getContent(), is(TEST_REPLY_DTO.getContent()));
        assertThat(TEST_REPLY.getUpdatedBy(), is(TEST_REPLY_DTO.getRegisterId()));
    }



    @Test
    public void 댓글_삭제_테스트() throws Exception {
        //given
        Reply TEST_REPLY = createReply();
        ReplyDTO TEST_REPLY_DTO = createReplyDTO();

        given(replyRepository.findById(any()))
                .willReturn(Optional.of(TEST_REPLY));

        //when
        replyService.deleteReply(TEST_REPLY_DTO);

        //then
        verify(replyRepository, atLeastOnce()).findById(any());
        assertTrue(TEST_REPLY.getIsRemove());
        assertThat(TEST_REPLY.getUpdatedBy(), is(TEST_REPLY_DTO.getRegisterId()));
    }

    private Reply createReply() {
        return Reply.builder()
                .replyId(TEST_REPLY_ID)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .name("test")
                .authId("test")
                .pwd("test")
                .age(10l)
                .role(MemberRole.USER)
                .gender(Gender.MAN)
                .address(new Address("", "", "", "", ""))
                .build();
    }

    private ReplyDTO createReplyDTO() {
        ReplyDTO replyDTO = new ReplyDTO();

        replyDTO.setBoardId(TEST_BOARD_ID);
        replyDTO.setContent("testContent");
        replyDTO.setParentDepth(0l);
        replyDTO.setParentReplyGroup(1l);
        replyDTO.setRegisterId("tester");
        replyDTO.setParentReplyGroupOrder(2l);
        replyDTO.setReplyId(TEST_REPLY_ID);

        return replyDTO;
    }

    private Board createBoard(Member TEST_MEMBER) {
        Board board = Board.builder()
                .content("test")
                .title("test")
                .tag("test")
                .member(TEST_MEMBER)
                .build();

        ReflectionTestUtils.setField(board, "boardId", TEST_BOARD_ID);

        return board;
    }

//    @Test
//    public void 댓글_삭제() throws Exception {
//        //given
//        Board board = createBoard();
//        Reply reply = Reply.builder()
//                .replyId(1L)
//                .board(board)
//                .content("변경 전")
//                .createdBy("testId")
//                .createdDate(LocalDateTime.now())
//                .build();
//        em.persist(reply);
//
//        ReplyDTO dto = ReplyDTO.builder()
//                .replyId(1L)
//                .boardId(board.getBoardId())
//                .isRemove(true)
//                .build();
//
//        em.flush();
//        em.clear();
//        List<ReplyDTO> list1 = replyService.list(board.getBoardId());
//        assertEquals(1,list1.size());
//
//        //when
//        replyService.deleteReply(dto);
//        Reply deletedReply = em.find(Reply.class, 1L);
//        List<ReplyDTO> list = replyService.list(board.getBoardId());
//
//        //then
//        assertEquals(1, list.size());
//        assertEquals(deletedReply.getIsRemove(), true);
//    }

}