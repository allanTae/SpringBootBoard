package com.allan.springBootBoard.web.repository;

import com.allan.springBootBoard.web.board.domain.model.BoardVO;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@MybatisTest
@Transactional
public class BoardRepositoryTest {

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    private SqlSession sqlSession;


    @Test
    public void 마이바티스_전체조회() throws Exception {
        //given

        //when
        List<BoardVO> boards = boardMapper.selectAllBoard();

        //then
        System.out.println("boards size: " + boards.size());
    }
}
