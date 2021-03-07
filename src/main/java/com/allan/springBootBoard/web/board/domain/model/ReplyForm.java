package com.allan.springBootBoard.web.board.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReplyForm {

    String content; // 댓글 내용
    String regId; // 작성자 아이
    Long boardId;  // 게시글 식별
}
