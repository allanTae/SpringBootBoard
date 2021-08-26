package com.allan.springBootBoard.web.board.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    private void setUp(){
        boardRepository.deleteAll();
    }

}
