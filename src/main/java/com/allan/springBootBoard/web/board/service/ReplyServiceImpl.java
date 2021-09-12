package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.ReplyRepository;
import com.allan.springBootBoard.web.error.code.ErrorCode;
import com.allan.springBootBoard.web.error.exception.BoardNotFoundException;
import com.allan.springBootBoard.web.error.exception.ReplyNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    @NonNull
    ReplyRepository replyRepository;

    @NonNull
    BoardRepository boardRepository;

    @Override
    public List<ReplyDTO> list(Long boardId) {
        return replyRepository.getReplyList(boardId);
    }

    /**
     * 게시글 댓글을 저장하기 위한 메소드 입니다.
     * 특정 게시글의 댓글로, '부모댓글' 또는 '게시글댓글' 로 분류되는 댓글을 저장하는 메소드입니다.
     * 각 게시글 댓글들은 고유 그룹번호를 가지고 있으며, 게시글 댓글과 관련된 모든 댓글들은 같은 그룹번호를 공유합니다.
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
                .build();

        board.addReply(reply);
        replyRepository.save(reply);

        return replyId;
    }

    /**
     * 답변 댓글을 저장하기 위한 메소드 입니다.
     * 이미 작성 된 댓글의 답변 댓글로, '자식댓글' 또는 '답변댓글' 로 분류되는 댓글을 저장하는 메소드입니다.
     * 답변 기준이 되는 댓글과 같은 그룹번호를 공유합니다.
     * @param replyDTO
     * @return
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
                .build();

        // 연관 관계 메소드를 사용.
        board.addReply(reply);

        // 댓글들 순서 조정을 위한 메소드.
        if(groupOrder == null ){
            reply.changeGroupOrder(listCnt+1);
        }else{
            replyRepository.updateGroupOrder("system", LocalDateTime.now(),
                                             replyDTO.getBoardId(), replyDTO.getParentReplyGroup(), replyDTO.getParentReplyGroupOrder());  // 그룹 내 순서를 조정하기 위한 update 문.
            reply.changeGroupOrder(groupOrder);
        }

        replyRepository.save(reply);

        return replyId;
    }

    /**
     * 댓글에 내용을 수정하는 위한 메소드 입니다.
     * @param replyDTO
     * @return
     */
    @Transactional
    @Override
    public Long updateReply(ReplyDTO replyDTO) {
        Reply findReply = replyRepository.findById(replyDTO.getReplyId()).orElseThrow(() -> new ReplyNotFoundException("해당 reply 엔티티를 찾을 수 없습니다.", ErrorCode.ENTITY_NOT_FOUND));

        findReply.changeContent(replyDTO.getContent());

        return findReply.getReplyId();
    }

    /**
     * 댓글을 삭제할 때 사용하는 메소드입니다.
     * 댓글 삭제 플래그만 변경 합니다.
     * @param replyDTO
     * @return replyId, this is ReplyEntity's ID that was successfully updated.
     */
    @Transactional
    @Override
    public Long deleteReply(ReplyDTO replyDTO) {
        Reply findReply = replyRepository.findById(replyDTO.getReplyId()).orElseThrow(() -> new ReplyNotFoundException("해당 reply 엔티티를 찾을 수 없습니다.", ErrorCode.ENTITY_NOT_FOUND));

        findReply.changeIsRemove(true);

        return findReply.getReplyId();
    }

}
