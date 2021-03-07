package com.allan.springBootBoard.web.service;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.repository.CategoryRepository;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Category;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.BoardVO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import com.allan.springBootBoard.web.board.service.BoardService;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.service.MemberService;
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
class BoardServiceImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setup(){
        boardService.deleteAll();
        memberService.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void 마이바티스_전체조회() throws Exception {
        //given

        //when
        List<BoardVO> boardVOS = boardMapper.selectAllBoard();

        //then
        assertEquals(0, boardVOS.size());
    }

    @Test
    public void 마이바티스_삭제() throws Exception {
        //given
        Member member = createMember(createAddress(), "fakeuser");
        Category category = createCategory();
        em.flush();
        em.clear();
        BoardDTO boardDTO = createBoardDTO(member.getId());

        boardService.save(category.getCategoryId(), boardDTO);

        //when
        boardMapper.deleteAll();

        //then
        List<BoardVO> boards = boardMapper.selectAllBoard();
        int size = boards.size();
        assertEquals(size, 0);
    }

    @Test
    public void 마이바티스_키워드_전체조회() throws Exception {
        //given
        Member member = createMember(createAddress(), "fakeuser");
        Member member2 = createMember(createAddress(), "fakeuser2");
        Category category = createCategory();

        BoardDTO boardDTO = BoardDTO.builder()
                .registerId(member.getId())
                .build();

        BoardDTO boardDTO2 = BoardDTO.builder()
                .title("testTitle")
                .content("testContent")
                .registerId(member2.getId())
                .tag("testTag")
                .build();

        boardService.save(category.getCategoryId(), boardDTO);
        boardService.save(category.getCategoryId(), boardDTO2);

        em.flush();
        em.clear();

        Search search = Search.builder()
                .searchType("title")
                .keyword("testTitle")
                .build();

        //when
        List<BoardVO> boardVOs = boardMapper.selectAllBoards(search);

        //then
        assertEquals(boardVOs.get(0).getTitle(), "testTitle");
    }

    @Test
    public void 게시글_등록() throws Exception {
        //given
        Member member = createMember(createAddress(), "fakeuser");
        Category category = createCategory();
        BoardDTO boardDTO = createBoardDTO(member.getId());

        //when
        Long boardPk = boardService.save(category.getCategoryId(), boardDTO);

        //then
        Board board = em.find(Board.class, boardPk);
        assertEquals(boardDTO.getTitle(), board.getTitle());
        assertEquals(boardDTO.getContent(), board.getContent());
        assertEquals(boardDTO.getTag(), board.getTag());
    }

    @Test
    public void 게시글_회원이름으로_조회() throws Exception {
        //given
        Member member = createMember(createAddress(), "fakeuser");
        Category category = createCategory();
        BoardDTO boardDTO = createBoardDTO(member.getId());

        //when
        boardService.save(category.getCategoryId(), boardDTO);

        //then
        Board findBoard = boardService.findByMemberId(member.getId()).get(0);
        assertEquals(boardDTO.getTitle(), findBoard.getTitle());
        assertEquals(boardDTO.getContent(), findBoard.getContent());
        assertEquals(boardDTO.getTag(), findBoard.getTag());
    }

    @Test
    public void 게시글_수정() throws Exception {
        //given
        Member member = createMember(createAddress(), "fakeuser");
        Category category = createCategory();
        BoardDTO boardDTO = createBoardDTO(member.getId());
        Long boardId = boardService.save(category.getCategoryId(), boardDTO);

        //when
        BoardDTO updateDTO = BoardDTO.builder()
                .boardId(boardId)
                .title("수정 된 제목")
                .content("수정 된 내용")
                .tag("수정 된 태그")
                .build();
        boardService.update(updateDTO, "관리자");

        //then
        Board findBoard = boardRepository.findOne(boardId);
        assertEquals(updateDTO.getTitle(), findBoard.getTitle());
        assertEquals(updateDTO.getContent(), findBoard.getContent());
        assertEquals(updateDTO.getTag(), findBoard.getTag());
    }

    @Test
    public void 게시물_삭제() throws Exception {
        //given
        Member member = createMember(createAddress(), "fakeuser");
        Category category = createCategory();
        BoardDTO boardDTO = createBoardDTO(member.getId());
        Long boardId = boardService.save(category.getCategoryId(), boardDTO);

        //when
        boardService.deleteById(boardId);
        List<Board> boards = boardService.findAll();

        //then
        assertEquals(boards.size(), 0);

    }

    private BoardDTO createBoardDTO(String memberId) {
        BoardDTO dto = BoardDTO.builder()
                .title("테스트 게시 제목")
                .content("테스트 게시글 내용")
                .tag("테스트 게시글 태그")
                .registerId(memberId)
                .build();

        return dto;
    }

    private Category createCategory() {
        Category category = Category.builder()
                .name("테스트게시")
                .createdBy("테스트관리")
                .createdDate(LocalDateTime.now())
                .build();

        em.persist(category);
        return category;
    }

    private Member createMember(Address address, String id) {
        Member member = Member.builder()
                .id(id)
                .pwd("test")
                .name("태지운")
                .age(29L)
                .gender(Gender.MAN)
                .address(address)
                .createdBy("fakeuser")
                .createdDate(LocalDateTime.now())
                .role(MemberRole.USER)
                .phoneNumber("01079978543")
                .build();

        em.persist(member);
        return member;
    }

    private Address createAddress() {
        return new Address("Gwanju", "Namunroo", "123456");
    }
}