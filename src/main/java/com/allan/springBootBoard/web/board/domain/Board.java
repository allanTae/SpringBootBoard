package com.allan.springBootBoard.web.board.domain;

import com.allan.springBootBoard.common.domain.BaseEntity;
import com.allan.springBootBoard.web.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<Reply>();

    private String title;
    private String content;
    private String tag;

    @JoinColumn(name = "view_cnt")
    @Column(nullable = false)
    private Long viewCnt;

    @PrePersist
    public void prePersist(){
        this.viewCnt = 0L;
    }

    @Builder
    public Board(Member member, Category category, String title, String content,
                 String tag, Long view_cnt, String createdBy, LocalDateTime createdDate,
                 String updatedBy, LocalDateTime updatedDate) {
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

    public void changeViewCnt(Long viewCnt){
        this.viewCnt = viewCnt;
    }

    public void addReply(Reply reply){
        if(this.replyList.contains(reply)){
        }else{
            this.replyList.add(reply);
            reply.changeBoard(this);
        }

    }
}
