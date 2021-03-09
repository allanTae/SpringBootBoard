package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;

import java.util.List;

public interface ReplyRepository {
    public List<ReplyDTO> getReplyList(Long boardId);

    public Long insertReply(Reply reply);

    public Long getMaxReplyId(Long boardId);

    public Long getReplyListCnt(ReplyDTO dto);

    public Long getGroupOrder(ReplyDTO dto);

    public void updateGroupOrder(ReplyDTO dto);

    public Long updateReply(ReplyDTO dto);

    public Long deleteReply(Long replyId, Long boardId);

    public void deleteAll(); // test용. 나중에 배포시 삭제 요망.

}
