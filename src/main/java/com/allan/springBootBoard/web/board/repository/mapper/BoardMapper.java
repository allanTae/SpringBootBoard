package com.allan.springBootBoard.web.board.repository.mapper;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardDTO> selectAllBoards(Search search);
    int deleteAll();
}
