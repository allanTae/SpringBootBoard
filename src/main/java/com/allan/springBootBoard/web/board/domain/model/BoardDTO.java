package com.allan.springBootBoard.web.board.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDTO {

    private Long boardId;
    private String title;
    private String content;
    private String tag;
    private String registerId;


    @Builder
    public BoardDTO(Long boardId, String title, String content, String tag, String registerId) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.registerId = registerId;
    }
}
