package com.allan.springBootBoard.web.board.domain;

import com.allan.springBootBoard.common.domain.BaseEntity;
import com.allan.springBootBoard.web.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;
    private String content;
    private String tag;

    @JoinColumn(name = "view_cnt")
    @Column(nullable = false)
    private int viewCnt;

    @PrePersist
    public void prePersist(){
        this.viewCnt = 0;
    }

    @Builder
    public Board(Member member, Category category, String title, String content,
                 String tag, int view_cnt, String createdBy, LocalDateTime createdDate,
                 String updatedBy, LocalDateTime updatedDate ) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.viewCnt = view_cnt;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    public void changeBoardContent(String title, String content, String tag, String updatedBy){
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.updatedBy = updatedBy;
        this.updatedDate = LocalDateTime.now();
    }

    public void changeViewCnt(int viewCnt){
        this.viewCnt = viewCnt;
    }
}
