package com.allan.springBootBoard.web.board.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MyBoardDTO {
    private Long boardId;
    private String title;
    private Long viewCnt;
    private String createdBy;
    private String createdDate;

    @Builder
    public MyBoardDTO(Long boardId, String title, Long viewCnt, String createdBy, String createdDate){
        this.boardId = boardId;
        this.title = title;
        this.viewCnt = viewCnt;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
