package com.allan.springBootBoard.web.repository;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.ReplyRepository;
import com.allan.springBootBoard.web.board.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
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
class ReplyRepositoryImplTest {

    @Autowired
    ReplyRepository replyRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardService boardService;

    @BeforeEach
    public void setup(){
        replyRepository.deleteAll();
        boardService.deleteAll();
    }

    @Test
    public void 전체_댓글수_조회() throws Exception {
        //given
        Board board = createBoard();
        Board board2 = createBoard();

        createAndSaveReply(board, 2L, "테스트 댓글 내용", "테스트 관리자");
        createAndSaveReply(board2, 3L, "테스트 댓글 내용2", "테스트 관리자");

        //when
        List<ReplyDTO> list = replyRepository.getReplyList(board.getBoardId());
        int size = list.size();

        //then
        assertEquals(1, size);
    }

    private void createAndSaveReply(Board board, Long replyId, String content, String cratedBy) {
        Reply reply = Reply.builder()
                .replyId(replyId)
                .content(content)
                .board(board)
                .createdBy(cratedBy)
                .createdDate(LocalDateTime.now())
                .build();
        replyRepository.insertReply(reply);
    }

    private Board createBoard() {
        Board board = Board.builder()
                .title("테스트 게시글 내용")
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(board);
        return board;
    }

    @Test
    public void 댓글_생성아이디_조회() throws Exception {
        // take1.
        // 데이터가 존재할떄.
        //given
        replyRepository.deleteAll();
        Board board = createBoard();

        createAndSaveReply(board, 2L, "test content1", "test manager");
        createAndSaveReply(board, 3L, "test content2", "test manager");

        //when
        Long replyId = replyRepository.getMaxReplyId(board.getBoardId());

        //then
        assertEquals(4L, replyId);

        // take2.
        // 데이터가 존재하지 않을 때
        // given
        replyRepository.deleteAll();
        Board board2 = createBoard();

        // when
        Long replyId2 = replyRepository.getMaxReplyId(board2.getBoardId());
        assertEquals(1L, replyId2);
    }

    @Test
    public void 특정게시글에대한_전체댓글수() throws Exception {
        //given
        Board board = createBoard();
        Board board2 = createBoard();

        Reply reply1 = Reply.builder()
                .board(board)
                .replyId(1L)
                .replyGroup(1L)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply1);

        Reply reply2 = Reply.builder()
                .board(board)
                .replyId(2L)
                .replyGroup(1L)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply2);

        Reply reply3 = Reply.builder()
                .board(board2)
                .replyId(3L)
                .replyGroup(3L)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply3);

        ReplyDTO dto = ReplyDTO.builder()
                .boardId(board.getBoardId())
                .parentReplyGroup(1L)
                .registerId("fakeuser")
                .build();

        //when
        Long replyListCnt = replyRepository.getReplyListCnt(dto);

        //then
        assertEquals(2L, replyListCnt);
    }

    @Test
    public void 자식댓글_순서() throws Exception {
        //given
        Board board = createBoard();

        Reply reply1 = Reply.builder()
                .board(board)
                .replyId(1L)
                .replyGroup(1L)
                .replyGroupOrder(1L)
                .depth(0L)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply1);

        Reply reply2 = Reply.builder()
                .board(board)
                .replyId(2L)
                .replyGroup(1L)
                .replyGroupOrder(2L)
                .depth(1L)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply2);

        Reply reply3 = Reply.builder()
                .board(board)
                .replyId(3L)
                .replyGroup(1L)
                .replyGroupOrder(3L)
                .depth(2L)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply3);

        Reply reply4 = Reply.builder()
                .board(board)
                .replyId(4L)
                .replyGroup(1L)
                .replyGroupOrder(4L)
                .depth(1L)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(reply4);

        ReplyDTO dto = ReplyDTO.builder()
                .boardId(board.getBoardId())
                .parentReplyGroup(1L)
                .parentReplyGroupOrder(2L)
                .parentDepth(1L)
                .registerId("fakeuser")
                .build();

        //when
        Long groupOrder = replyRepository.getGroupOrder(dto);

        //then
        assertEquals(4L, groupOrder);
    }
}