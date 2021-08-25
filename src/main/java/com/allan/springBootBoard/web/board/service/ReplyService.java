package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;

import java.util.List;

public interface ReplyService {

    /**
     * 특정 게시글에 대한 댓글 리스트를 반환하는 메소드입니다.
     * @param boardId
     * @return List<ReplyDTO>
     */
    public List<ReplyDTO> list(Long boardId);

    /**
     * 게시글 댓글을 저장하기 위한 메소드 입니다.
     * @param replyDTO
     * @return replyId, this is ReplyEntity's ID that was successfully registered.
     */
    public Long saveParentReply(ReplyDTO replyDTO);

    /**
     * 답변 댓글을 저장하기 위한 메소드 입니다.
     * @param replyDTO
     * @return
     */
    public Long saveChildReply(ReplyDTO replyDTO);

    /**
     * 댓글의 내용을 수정하는 메소드 입니다.
     * @param replyDTO
     * @return
     */
    public Long updateReply(ReplyDTO replyDTO);

    /**
     * 댓글을 삭제하는 메소드 입니다.
     * @param
     * @return
     */
    public Long deleteReply(ReplyDTO replyDTO);

}
