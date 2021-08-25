package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import java.util.List;

public interface BoardService {

    /**
     * 게시글을 저장하는 메소드입니다.
     * @param categoryPk
     * @param boardDTO
     * @param registerId
     * @return
     */
    public Long save(Long categoryPk, BoardDTO boardDTO, String registerId);

    /**
     * Board 엔티티의 식별자로 게시글을 조회하는 메소드입니다.
     * @param boardId
     * @return
     */
    public Board findOne(Long boardId);

    /**
     * 게시글 내용을 수정하는 메소드 입니다.
     * @param boardDTO
     * @param updatedBy
     * @return
     */
    public Long update(BoardDTO boardDTO, String updatedBy);

    /**
     * mybatis를 사용하여 검색 키워드를 사용하여 게시글을 조회하는 메소드입니다.
     * @param search
     * @return
     */
    public List<BoardDTO> findAllByMybatis(Search search);

    /**
     * mybatis를 사용하여 모든 게시글을 삭제하는 메소드입니다.
     * @param
     * @return
     */
    public void deleteAll();

    /**
     * Board 엔티티의 식별자로 특정 게시글을 삭제하는 메소드입니다.
     * @param boardId
     * @return
     */
    public Long deleteById(Long boardId);
}
