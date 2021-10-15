package com.allan.springBootBoard.web.board.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BoardForm {

    private Long boardId;

    @NotEmpty(message = "제목을 입력 해 주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력 해 주세요.")
    private String content;
    private String tag;
    private Long viewCnt;
    private String name;

    @Builder
    public BoardForm(Long boardId, String title, String content, String tag, Long viewCnt, String name) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.viewCnt = viewCnt;
        this.name = name;
    }
}
