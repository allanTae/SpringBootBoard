package com.allan.springBootBoard.web.service;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.ReplyRepository;
import com.allan.springBootBoard.web.board.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class ReplyServiceImplTest {

    @Autowired
    ReplyService replyService;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 부모댓글_생성() throws Exception {
        //given
        Board board = createBoard();

        Reply reply1 = Reply.builder()
                .board(board)
                .replyId(1L)
                .replyGroup(1L)
                .replyGroupOrder(1L)
                .depth(0L)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply1);

        Reply reply2 = Reply.builder()
                .board(board)
                .replyId(2L)
                .replyGroup(1L)
                .replyGroupOrder(2L)
                .depth(1L)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply2);

        ReplyDTO dto = ReplyDTO.builder()
                .boardId(board.getBoardId())
                .content("test content")
                .registerId("testId")
                .build();

        //when
        Long replyId = replyService.saveParentReply(dto);
        em.flush();
        em.clear();
        Reply reply = em.find(Reply.class, replyId);

        //then
        assertEquals(3L, reply.getReplyGroup());
        assertEquals(1L, reply.getReplyGroupOrder());
    }

    @Test
    public void 자식댓글_생성() throws Exception {
        //given
        Board board = createBoard();

        Reply reply1 = Reply.builder()
                .board(board)
                .replyId(1L)
                .replyGroup(1L)
                .replyGroupOrder(1L)
                .depth(0L)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply1);

        Reply reply2 = Reply.builder()
                .board(board)
                .replyId(2L)
                .replyGroup(1L)
                .replyGroupOrder(2L)
                .depth(1L)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply2);

        Reply reply3 = Reply.builder()
                .board(board)
                .replyId(3L)
                .replyGroup(1L)
                .replyGroupOrder(3L)
                .depth(2L)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply3);

        Reply reply4 = Reply.builder()
                .board(board)
                .replyId(4L)
                .replyGroup(1L)
                .replyGroupOrder(4L)
                .depth(1L)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply4);

        Reply reply5 = Reply.builder()
                .board(board)
                .replyId(5L)
                .replyGroup(1L)
                .replyGroupOrder(5L)
                .depth(1L)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply5);

        ReplyDTO dto = ReplyDTO.builder()
                .boardId(board.getBoardId())
                .parentReplyGroup(1L)
                .parentReplyGroupOrder(2L)
                .parentDepth(1L)
                .registerId("testId")
                .build();

        //when
        replyService.saveChildReply(dto);
        em.clear();
        Reply findReply3 = em.find(Reply.class, reply3.getReplyId());
        Reply findReply4 = em.find(Reply.class, reply4.getReplyId());
        Reply findReply5 = em.find(Reply.class, reply5.getReplyId());

        //then
        assertEquals(4L, findReply3.getReplyGroupOrder());
        assertEquals(5L, findReply4.getReplyGroupOrder());
        assertEquals(6L, findReply5.getReplyGroupOrder());
    }

    @Test
    public void 댓글_수정() throws Exception {
        //given
        Board board = createBoard();
        Reply reply = Reply.builder()
                .replyId(1L)
                .board(board)
                .content("변경 전")
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply);

        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyId(1L)
                .content("변경 후")
                .registerId("testId2")
                .build();

        //when후
        replyService.updateReply(replyDTO);
        em.flush();
        em.clear();
        Reply updatedReply = em.find(Reply.class, 1L);

        //then
        assertEquals("변경 후", updatedReply.getContent());
        assertEquals("testId2", updatedReply.getUpdatedBy());
    }

    @Test
    public void 댓글_삭제() throws Exception {
        //given
        Board board = createBoard();
        Reply reply = Reply.builder()
                .replyId(1L)
                .board(board)
                .content("변경 전")
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply);

        ReplyDTO dto = ReplyDTO.builder()
                .replyId(1L)
                .boardId(board.getBoardId())
                .isRemove(true)
                .build();

        em.flush();
        em.clear();
        List<ReplyDTO> list1 = replyService.list(board.getBoardId());
        assertEquals(1,list1.size());

        //when
        replyService.deleteReply(dto);
        Reply deletedReply = em.find(Reply.class, 1L);
        List<ReplyDTO> list = replyService.list(board.getBoardId());

        //then
        assertEquals(1, list.size());
        assertEquals(deletedReply.getIsRemove(), true);
    }

    private Board createBoard() {
        Board board = Board.builder()
                .title("테스트 게시글 내용")
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(board);
        return board;
    }
}