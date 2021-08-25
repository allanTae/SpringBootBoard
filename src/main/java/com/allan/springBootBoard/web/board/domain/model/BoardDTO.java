package com.allan.springBootBoard.web.board.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class BoardDTO {

    private Long boardId;
    private Long categoryId;
    private String title;
    private String content;
    private String tag;
    private int viewCnt;
    private String createdBy;
    private String createdDate;

    @Builder
    public BoardDTO(Long boardId, String title, String content, String tag) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }

    public void setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
