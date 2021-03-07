package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BoardRepositoryImpl implements BoardRepository{

    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardMapper boardMapper;

    /**
     * 게시글 등록
     * @param board
     * @return
     */
    @Override
    public Long save(Board board) {
        em.persist(board);
        return board.getBoardId();
    }

    /**
     * 단일 게시글 조회
     * @param boardPk
     * @return
     */
    @Override
    public Board findOne(Long boardPk) {
        return em.find(Board.class, boardPk);
    }

    @Override
    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }

    /**
     * 회원 아이디로 게시글 조회
     * @param memberId
     * @return
     */
    @Override
    public List<Board> findByMemberId(String memberId) {
        return em.createQuery("select b from Board b join b.member m on m.id = :memberId", Board.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /**
     * 게시글 수정
     * @param dto
     * @return
     */
    @Override
    public Long update(BoardDTO dto, String updatedBy) {
        Board findBoard = em.find(Board.class, dto.getBoardId());
        findBoard.changeBoardContent(dto.getTitle(), dto.getContent(), dto.getTag(), updatedBy);
        return findBoard.getBoardId();
    }

    /**
     * 게시글 삭제.
     * @param boardId
     * @return
     */
    @Override
    public Long delete(Long boardId) {
        Board findBoard = em.find(Board.class, boardId);
        em.remove(findBoard);
        return null;
    }

    @Override
    public void deleteAll() {
        boardMapper.deleteAll();
    }


}
