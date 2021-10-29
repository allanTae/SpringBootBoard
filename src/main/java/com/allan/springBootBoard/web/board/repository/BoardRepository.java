package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Board;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    public Optional<Board> findByMember(Long memberId);

    @Query(value = "select b from Board b join fetch b.member where b.boardId = :boardId")
    public Optional<Board> getBoard(@Param("boardId")Long boardId);
}
