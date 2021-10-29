package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.common.Pagination;
import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.MyBoardDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class MyBoardServiceTest {

    @Mock
    BoardMapper boardMapper;

    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    MyBoardService myBoardService;

    @Test
    public void 로그인한_회원_게시글수_테스트() throws Exception {
        //given
        int TEST_BOARDS_COUNT = 1;
        given(boardMapper.selectMyBoardListCnt(any()))
                .willReturn(TEST_BOARDS_COUNT);

        //when
        myBoardService.getMyBoardListCnt(any());

        //then
        verify(boardMapper, atLeastOnce()).selectMyBoardListCnt(any());
    }

    @Test
    public void 로그인한_회원_게시글_목록_테스트() throws Exception {
        //given
        Search TEST_SEARCH = new Search("title", "");
        TEST_SEARCH.pageInfo(1, 1, 10);
        List<BoardDTO> TEST_myBoardDTOList = List.of(
                new BoardDTO(1l, "test", 2l, "testerId", LocalDateTime.now(),"testerName", "testerId", MemberRole.USER),
                new BoardDTO(2l, "test", 3l, "testerId", LocalDateTime.now(),"testerName", "testerId", MemberRole.USER)
        );
        given(boardMapper.selectMyBoardListByLoginId(any()))
                .willReturn(TEST_myBoardDTOList);

        //when
        myBoardService.getMyBoardList(any(), TEST_SEARCH);

        //then
        verify(boardMapper, atLeastOnce()).selectMyBoardListByLoginId(any());
    }
}
