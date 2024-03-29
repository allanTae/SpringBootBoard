package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.error.exception.BoardNotFoundException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Category;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.CategoryRepository;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import com.allan.springBootBoard.web.member.service.MemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.allan.springBootBoard.web.error.code.ErrorCode.ENTITY_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    @NonNull
    BoardRepository boardRepository;

    @NonNull
    MemberService memberService;

    @NonNull
    CategoryRepository categoryRepository;

    @NonNull
    BoardMapper boardMapper;

    /**
     * 게시물 생성.
     * @param categoryPk
     * @param boardDTO
     * @param authId
     * @return
     */
    @Transactional
    @Override
    public Long save(Long categoryPk, BoardDTO boardDTO, String authId) {

        // 엔티티 조회
        Member findMember;
        findMember = memberService.findByAuthId(authId);

        Category findCategory = categoryRepository.findOne(categoryPk);

        // 게시물 생성
        Board board = Board.builder()
                .member(findMember)
                .category(findCategory)
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .tag(boardDTO.getTag())
                .build();

        boardRepository.save(board);
        return board.getBoardId();
    }

    /**
     * Board 엔티티의 식별자로 게시글을 조회하는 메소드입니다.
     * @param boardId
     * @return
     */
    @Transactional
    @Override
    public BoardDTO findOne(Long boardId) {
        Board findBoard = boardRepository.getBoard(boardId).orElseThrow(() -> new BoardNotFoundException( "해당 Board 엔티티가 존재하지 안습니다.", ENTITY_NOT_FOUND));
        BoardDTO boardDTO = BoardDTO.builder()
                .boardId(findBoard.getBoardId())
                .title(findBoard.getTitle())
                .content(findBoard.getContent())
                .tag(findBoard.getTag()).build();
        boardDTO.setCreatedDate(findBoard.getCreatedDate());
        boardDTO.setNickName(findBoard.getMember().getName(), findBoard.getMember().getAuthId(), findBoard.getMember().getRole());

        findBoard.changeViewCnt(findBoard.getViewCnt() + 1);
        return boardDTO;
    }

    /**
     * 게시물 수정.
     * @param dto
     * @return
     */
    @Transactional
    @Override
    public Long update(BoardDTO dto) {
        Board findBoard = boardRepository.findById(dto.getBoardId()).orElseThrow(() -> new BoardNotFoundException("해당 Board 엔티티가 존재하지 않습니다.", ENTITY_NOT_FOUND));
        findBoard.changeBoardContent(dto.getTitle(), dto.getContent(), dto.getTag());
        return dto.getBoardId();
    }

    /**
     * 게시물 삭제.
     * @param boardId
     * @return
     */
    @Transactional
    @Override
    public Long deleteById(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("해당 Board 엔티티가 존재하지 않습니다.", ENTITY_NOT_FOUND));
        boardRepository.delete(findBoard);
        return findBoard.getBoardId();
    }

    /**
     * mybatis 로 게시물 조회.
     * @param search
     * @return
     */
    @Override
    public List<BoardDTO> findBoardList(Search search) {
        log.info("startList: " + search.getStartList());
        return boardMapper.selectBoardList(search);
    }

    /**
     * mybatis 로 게시물 조회.
     * @param search
     * @return
     */
    @Override
    public int findBoardListCnt(Search search) {
        return boardMapper.selectBoardListCnt(search);
    }

    /**
     * mybatis 로 모든 게시물 삭제/
     * @return
     */
    @Transactional
    @Override
    public void deleteAll() {
        boardMapper.deleteAll();
    }



}
