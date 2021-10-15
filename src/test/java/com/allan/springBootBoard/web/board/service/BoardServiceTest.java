package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.repository.CategoryRepository;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Category;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
class BoardServiceTest {

    private String TEST_MEMBER_AUTH_ID = "TEST_ID";
    private Long TEST_BOARD_ENTITY_ID = 1l;

    private BoardService boardService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private BoardMapper boardMapper;

    @BeforeEach
    private void setUp(){
        MockitoAnnotations.openMocks(this);
        boardService =  new BoardServiceImpl(boardRepository, memberService, categoryRepository, boardMapper);
    }

    @Test
    public void 게시글등록_테스트() throws Exception {
        //given
        Long TEST_CATEGORY_ID = 2l;
        Member TEST_MEMBER = createMember(createAddress(), TEST_MEMBER_AUTH_ID);
        given(memberService.findByAuthId(any(String.class)))
                .willReturn(TEST_MEMBER);
        Category TEST_CATEGORY = createCategory();
        given(categoryRepository.findOne(any(Long.class)))
                .willReturn(TEST_CATEGORY);
        BoardDTO TEST_BOARD_DTO = createBoardDTO();

        //when
        boardService.save(TEST_CATEGORY_ID, TEST_BOARD_DTO, TEST_MEMBER_AUTH_ID);

        //then
        verify(boardRepository, atLeastOnce()).save(any(Board.class));
    }


    @Test
    public void 게시글수정_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember(createAddress(), TEST_MEMBER_AUTH_ID);
        Board TEST_BOARD = createBoard(TEST_MEMBER);
        BoardDTO TEST_UPDATE_INFO = BoardDTO.builder()
                .title("modified data")
                .content("modified data")
                .tag("modified data")
                .boardId(TEST_BOARD_ENTITY_ID)
                .build();
        given(boardRepository.findById(TEST_BOARD_ENTITY_ID))
                .willReturn(Optional.of(TEST_BOARD));

        //when
        boardService.update(TEST_UPDATE_INFO);

        //then
        verify(boardRepository, atLeastOnce()).findById(TEST_BOARD_ENTITY_ID);
        assertThat(TEST_BOARD.getTitle(), is("modified data"));
        assertThat(TEST_BOARD.getContent(), is("modified data"));
        assertThat(TEST_BOARD.getTag(), is("modified data"));
    }

    @Test
    public void 게시물삭제_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember(createAddress(),TEST_MEMBER_AUTH_ID);
        Board TEST_BOARD = createBoard(TEST_MEMBER);
        given(boardRepository.findById(any(Long.class)))
                .willReturn(Optional.of(TEST_BOARD));

        //when
        boardService.deleteById(TEST_BOARD_ENTITY_ID);

        //then
        verify(boardRepository, atLeastOnce()).delete(TEST_BOARD);
    }

    @Test
    public void 마이바티스게시물_조회() throws Exception {
        //given
        Search TEST_SEARCH = new Search("test", "test");
        List<BoardDTO> TEST_BOARD_DTOS = new ArrayList<>();
        given(boardMapper.selectBoardList(any(Search.class)))
                .willReturn(TEST_BOARD_DTOS);

        //when
        boardService.findBoardList(TEST_SEARCH);

        //then
        verify(boardMapper, atLeastOnce()).selectBoardList(TEST_SEARCH);
    }

    @Test
    public void 마이바티스_삭제() throws Exception {
        //given
        Search TEST_SEARCH = new Search("test", "test");
        int TEST_DELETE_COUNT = 1;
        given(boardMapper.deleteAll())
                .willReturn(TEST_DELETE_COUNT);

        //when
        boardService.deleteAll();

        //then
        verify(boardMapper, atLeastOnce()).deleteAll();
    }

    private Board createBoard(Member TEST_MEMBER) {
        Board board = Board.builder()
                .title("TEST")
                .content("TEST")
                .tag("TEST")
                .member(TEST_MEMBER)
                .build();

        ReflectionTestUtils.setField(board, "boardId", TEST_BOARD_ENTITY_ID);

        return board;
    }

    private BoardDTO createBoardDTO() {
        BoardDTO dto = BoardDTO.builder()
                .title("테스트 게시 제목")
                .content("테스트 게시글 내용")
                .tag("테스트 게시글 태그")
                .build();

        return dto;
    }

    private Category createCategory() {
        Category category = Category.builder()
                .name("테스트게시")
                .build();

        return category;
    }

    private Member createMember(Address address, String id) {
        Member member = Member.builder()
                .authId(id)
                .pwd("test")
                .name("tester")
                .age(29L)
                .gender(Gender.MAN)
                .address(address)
                .role(MemberRole.USER)
                .phoneNumber("01079978543")
                .build();

        return member;
    }

    private Address createAddress() {
        return new Address("도시", "거리", "우편번호", "", "");
    }
}