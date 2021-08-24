package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.ReplyRepository;
import com.allan.springBootBoard.web.error.code.ErrorCode;
import com.allan.springBootBoard.web.error.exception.BoardNotFoundException;
import com.allan.springBootBoard.web.error.exception.ReplyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    BoardRepository boardRepository;

    @Override
    public List<ReplyDTO> list(Long boardId) {
        return replyRepository.getReplyList(boardId);
    }

    /**
     * 자식 댓글을 저장하기 위한 메소드 입니다.
     * @param replyDTO
     * @return replyId, this is ReplyEntity's ID that was successfully registered.
     */
    @Transactional
    @Override
    public Long saveParentReply(ReplyDTO replyDTO) {
        Board board = boardRepository.findById(replyDTO.getBoardId()).orElseThrow(() ->new BoardNotFoundException("해당 Board 엔티티가 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));

        // 댓글 아이디를 위해 호출
        // 기존에 엔티티에서 기본키는 자동으로 생성되도록 설정했으나,
        // 댓글 그룹번호, 그룹번호 내 순서를 설정하기 위해서 별도 sql로 호출하여 설정한다.
        Long replyId = replyRepository.getMaxReplyId();

        Reply reply = Reply.builder()
                .replyId(replyId)
                .replyGroup(replyId)    // 댓글 그룹번호 설정.
                .replyGroupOrder(1L)    // 댓글 그룹번호 내 초기 순서 설정.
                .content(replyDTO.getContent())
                .createdBy(replyDTO.getRegisterId())
                .createdDate(LocalDateTime.now())
                .build();

        board.addReply(reply);

        replyRepository.save(reply);

        return replyId;
    }

    /**
     * @param replyDTO
     * @return replyId, this is ReplyEntity's ID that was successfully registered.
     */
    @Transactional
    @Override
    public Long saveChildReply(ReplyDTO replyDTO) {
        Board board = boardRepository.findById(replyDTO.getBoardId()).orElseThrow(() -> new BoardNotFoundException("해당 Board 엔티티가 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
        Long replyId = replyRepository.getMaxReplyId();
        Long listCnt = replyRepository.getReplyListCnt(replyDTO.getBoardId(), replyDTO.getParentReplyGroup());
        Long groupOrder = replyRepository.getGroupOrder(replyDTO.getBoardId(), replyDTO.getParentReplyGroup(), replyDTO.getParentReplyGroupOrder(), replyDTO.getParentDepth());

        Reply reply = Reply.builder()
                .replyId(replyId)
                .content(replyDTO.getContent())
                .depth(replyDTO.getParentDepth()+1)
                .replyGroup(replyDTO.getParentReplyGroup())
                .createdBy(replyDTO.getRegisterId())
                .createdDate(LocalDateTime.now())
                .build();

        // 연관 관계 메소드를 사용.
        board.addReply(reply);

        if(groupOrder == null ){
            reply.changeGroupOrder(listCnt+1);
        }else{
            replyRepository.updateGroupOrder(replyDTO.getUpdatedBy(), replyDTO.getUpdatedDate(),
                                             replyDTO.getBoardId(), replyDTO.getParentReplyGroup(), replyDTO.getParentReplyGroupOrder());  // 그룹 내 순서를 조정하기 위한 update 문.
            reply.changeGroupOrder(groupOrder);
        }

        replyRepository.save(reply);

        return replyId;
    }

    @Transactional
    @Override
    public Long updateReply(ReplyDTO dto) {
        Reply findReply = replyRepository.findById(dto.getReplyId()).orElseThrow(() -> new ReplyNotFoundException("해당 reply 엔티티를 찾을 수 없습니다.", ErrorCode.ENTITY_NOT_FOUND));

        findReply.changeContent(dto.getContent());
        findReply.changeUpdateInfo(dto.getRegisterId(), LocalDateTime.now());

//        dto.setUpdatedBy(dto.getRegisterId());
//        dto.setUpdatedDate(LocalDateTime.now());
//
//        Long replyId = replyRepository.updateReply(dto);

        return findReply.getReplyId();
    }

    /**
     * 댓글 삭제 플래그만 변경 합니다.
     * @param replyDTO
     * @return replyId, this is ReplyEntity's ID that was successfully updated.
     */
    @Transactional
    @Override
    public Long deleteReply(ReplyDTO replyDTO) {
        Reply findReply = replyRepository.findById(replyDTO.getReplyId()).orElseThrow(() -> new ReplyNotFoundException("해당 reply 엔티티를 찾을 수 없습니다.", ErrorCode.ENTITY_NOT_FOUND));

        findReply.changeIsRemove(true);
        findReply.changeUpdateInfo(replyDTO.getRegisterId(), LocalDateTime.now());

        // 댓글 삭제 플래그만 변경한다.
//        replyDTO.setIsRemove(true);
//        replyDTO.setUpdatedBy(replyDTO.getRegisterId());
//        replyDTO.setUpdatedDate(LocalDateTime.now());
//        replyRepository.updateReply(replyDTO);

        return findReply.getReplyId();
    }

    @Transactional
    @Override
    public void deleteAll() {
        replyRepository.deleteAll();
    }
}
