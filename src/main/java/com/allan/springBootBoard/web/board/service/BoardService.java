package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;

import java.util.List;

public interface BoardService {

    public Long save(Long categoryPk, BoardDTO dto);
    public Board findOne(Long boardId);
    public List<Board> findByMemberId(String memberId);
    public List<Board> findAll();
    public Long update(BoardDTO dto, String updatedBy);
    public List<BoardDTO> findAllByMybatis(Search search);
    public void deleteAll();
    public Long deleteById(Long booardId);
    public Long updateViewCnt(Long boardId, String updatedBy);
}
