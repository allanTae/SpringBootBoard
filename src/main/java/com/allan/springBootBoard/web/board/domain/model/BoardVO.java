package com.allan.springBootBoard.web.board.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
//@NoArgsConstructor(force = true)
//@RequiredArgsConstructor
@NoArgsConstructor
public class BoardVO {

    private Long boardId;
    private Long memberId;
    private Long categoryId;
    public String title;
    private String content;
    private String tag;
    private int viewCnt;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;

}
