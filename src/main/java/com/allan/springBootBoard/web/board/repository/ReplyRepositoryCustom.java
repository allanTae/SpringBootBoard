package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;

public interface ReplyRepositoryCustom {
    public Long getMaxReplyId();
//    public Long updateReply(ReplyDTO dto);
    public Long deleteReply(Long replyId, Long boardId);
}
