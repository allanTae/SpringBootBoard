package com.allan.springBootBoard.web.board.repository;


import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;

import java.util.List;

public interface BoardRepository {
    public Long save(Board board);
    public Board findOne(Long boardId);
    public List<Board> findAll();
    public List<Board> findByMemberId(String memberId);
    public Long update(BoardDTO dto, String updatedBy);
    public Long delete(Long boardId);
    public void deleteAll();
}
