package com.allan.springBootBoard.web.board.repository;

public interface ReplyRepositoryJPQL {
    public Long getMaxReplyId();
    public Long deleteReply(Long replyId, Long boardId);
}
