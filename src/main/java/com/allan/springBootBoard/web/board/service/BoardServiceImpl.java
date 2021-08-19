package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Category;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.CategoryRepository;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import com.allan.springBootBoard.web.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BoardMapper boardMapper;

    /**
     * 게시물 생성.
     * @param categoryPk
     * @param dto
     * @return
     */
    @Transactional
    @Override
    public Long save(Long categoryPk, BoardDTO dto) {

        // 엔티티 조회
        Member findMember;
        try{
            findMember = memberService.findById(dto.getRegisterId());
        }catch(UserNotFoundException exception){
            throw new IllegalStateException(dto.getRegisterId() + " is not exist!!");
        }

        Category findCategory = categoryRepository.findOne(categoryPk);

        // 게시물 생성
        Board board = Board.builder()
                .member(findMember)
                .category(findCategory)
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdBy(dto.getRegisterId())
                .createdDate(LocalDateTime.now())
                .tag(dto.getTag())
                .build();

        boardRepository.save(board);
        return board.getBoardId();
    }

    @Transactional
    @Override
    public Board findOne(Long boardId) {
        Board board = boardRepository.findOne(boardId);
        board.changeViewCnt(board.getViewCnt() + 1);
        return board;
    }

    /**
     * 회원 아이디로 게시물 조회.
     * @param memberId
     * @return
     */
    @Override
    public List<Board> findByMemberId(String memberId) {
        return boardRepository.findByMemberId(memberId);
    }

    /**
     * 모든 게시물 조회.
     * @return
     */
    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    /**
     * 게시물 수정.
     * @param dto
     * @param updatedBy
     * @return
     */
    @Transactional
    @Override
    public Long update(BoardDTO dto, String updatedBy) {
        boardRepository.update(dto, updatedBy);
        return dto.getBoardId();
    }

    /**
     * 게시물 삭제.
     * @param booardId
     * @return
     */
    @Transactional
    @Override
    public Long deleteById(Long booardId) {
        return boardRepository.delete(booardId);
    }

    /**
     * mybatis 로 게시물 조회.
     * @param search
     * @return
     */
    @Override
    public List<BoardDTO> findAllByMybatis(Search search) {
        return boardMapper.selectAllBoards(search);
    }

    /**
     * mybatis 로 모든 게시물 삭제/
     * @return
     */
    @Transactional
    @Override
    public void deleteAll() {
        boardRepository.deleteAll();
    }

    /**
     * 조회수 수정
     * @param boardId
     * @param updatedBy
     * @return
     */
    @Transactional
    @Override
    public Long updateViewCnt(Long boardId, String updatedBy) {
        return null;
    }
}
