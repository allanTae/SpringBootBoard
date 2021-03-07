package com.allan.springBootBoard.web.board.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardForm {

    private Long boardId;
    private String title;
    private String content;
    private String tag;
    private Long viewCnt;
    private String registerId;

    @Builder
    public BoardForm(Long boardId, String title, String content, String tag, Long viewCnt, String registerId) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.viewCnt = viewCnt;
        this.registerId = registerId;
    }
}
