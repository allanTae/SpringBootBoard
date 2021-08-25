package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
