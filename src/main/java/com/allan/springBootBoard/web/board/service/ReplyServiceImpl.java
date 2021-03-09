package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.ReplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
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

    @Override
    public Long saveParentReply(ReplyDTO dto) {

        Board board = boardRepository.findOne(dto.getBoardId());

        // 댓글 아이디를 위해 호출
        // 기존에 엔티티에서 기본키는 자동으로 생성되도록 설정했으나,
        // 댓글 그룹번호, 그룹번호 내 순서를 설정하기 위해서 별도 sql로 호출하여 설정한다.
        Long replyId = replyRepository.getMaxReplyId(dto.getBoardId());

        log.info("replyId: " + replyId);

        Reply reply = Reply.builder()
                .replyId(replyId)
                .replyGroup(replyId)    // 댓글 그룹번호 설정.
                .replyGroupOrder(1L)    // 댓글 그룹번호 내 초기 순서 설정.
                .content(dto.getContent())
                .createdBy(dto.getRegisterId())
                .createdDate(LocalDateTime.now())
                .build();

        board.addReply(reply);

        replyRepository.insertReply(reply);

        return replyId;
    }

    @Override
    public Long saveChildReply(ReplyDTO dto) {

        Board board = boardRepository.findOne(dto.getBoardId());
        Long replyId = replyRepository.getMaxReplyId(dto.getBoardId());

        Reply reply = Reply.builder()
                .replyId(replyId)
                .content(dto.getContent())
                .depth(dto.getParentDepth()+1)
                .replyGroup(dto.getParentReplyGroup())
                .createdBy(dto.getRegisterId())
                .createdDate(LocalDateTime.now())
                .build();

        // 연관 관계 메소드를 사용.
        board.addReply(reply);

        Long listCnt = replyRepository.getReplyListCnt(dto);
        Long groupOrder = replyRepository.getGroupOrder(dto);

        log.info("groupOrder: " + groupOrder);
        log.info("listCnt: " + listCnt);

        if(groupOrder == null ){
            reply.changeGroupOrder(listCnt+1);
        }else{
            replyRepository.updateGroupOrder(dto);  // 그룹 내 순서를 조정하기 위한 update 문.
            reply.changeGroupOrder(groupOrder);
        }

        replyRepository.insertReply(reply);

        return replyId;
    }

    @Override
    public Long updateReply(ReplyDTO dto) {

        dto.setUpdatedBy(dto.getRegisterId());
        dto.setUpdatedDate(LocalDateTime.now());

        Long replyId = replyRepository.updateReply(dto);

        return replyId;
    }

    @Override
    public Long deleteReply(Long replyId, Long boardId) {
        replyRepository.deleteReply(replyId, boardId);
        return replyId;
    }

    @Override
    public void deleteAll() {
        replyRepository.deleteAll();
    }
}
