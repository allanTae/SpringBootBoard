package com.allan.springBootBoard.web.board.repository.mapper;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.MyBoardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardDTO> selectBoardList(Search search);
    int selectBoardListCnt(Search search);
    int deleteAll();
    List<BoardDTO> selectMyBoardListByLoginId(HashMap<String, Object> pagingInfo);
    int selectMyBoardListCnt(Long memberId);
}
