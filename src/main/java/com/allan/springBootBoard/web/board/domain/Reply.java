package com.allan.springBootBoard.web.board.domain;

import com.allan.springBootBoard.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

    // pk 값을 자동으로 생성하지 않고,
    // 데이터베이스 생성시 조회하여 값을 처리하도록 변경하였다.
    // 그 이유는 부모 댓글을 데이터베이스에 저장할때
    // 그룹 번호가 id 값과 동일한 값을 부여하기 위해서 이다.
    @Id
    @Column(name = "reply_id")
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    @JsonIgnore
    private Board board;

    private String content;

    @Column(name = "reply_group")
    private Long replyGroup;

    @Column(name = "reply_group_order")
    @ColumnDefault("1")
    private Long replyGroupOrder;

    @ColumnDefault("0")
    private Long depth;

    @Column(name = "reply_like")
    @ColumnDefault("0")
    private Long replyLike;

    public static final int MAX_DEPTH = 3;

    @PrePersist
    public void prePersist(){
        this.depth = this.depth == null ? 0L:this.depth;
        this.replyGroupOrder = this.replyGroupOrder == null ? 1L:this.replyGroupOrder;
        this.replyLike = this.replyLike == null ?0L:this.replyLike;
    }

    @Builder
    public Reply(Long replyId, Board board, String content, Long replyGroup, Long replyGroupOrder,
                 Long depth, Long replyLike, LocalDateTime createdDate,
                 String createdBy, LocalDateTime updatedDate, String updatedBy) {
        this.replyId = replyId; // 계층형 댓글 그룹번호를 설정하기 위 별도로 값을 부여하기 위함.
        this.board = board;
        this.content = content;
        this.replyGroup = replyGroup;
        this.replyGroupOrder = replyGroupOrder;
        this.depth = depth;
        this.replyLike = replyLike;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    public void changeGroupOrder(Long replyGroupOrder){
        this.replyGroupOrder = replyGroupOrder;
    }

    public void changeContent(String content) {this.content = content; }

    public void changeUpdateInfo(String updatedBy, LocalDateTime updatedDate){
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    public void changeBoard(Board board){
        this.board = board;
    }
}
