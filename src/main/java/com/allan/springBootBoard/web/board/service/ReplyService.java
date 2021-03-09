package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;

import java.util.List;

public interface ReplyService {

    public List<ReplyDTO> list(Long boardId);

    public Long saveParentReply(ReplyDTO dto);

    public Long saveChildReply(ReplyDTO dto);

    public Long updateReply(ReplyDTO dto);

    public Long deleteReply(Long replyId, Long boardId);

    public void deleteAll();

}
