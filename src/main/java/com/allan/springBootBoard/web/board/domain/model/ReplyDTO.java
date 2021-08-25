package com.allan.springBootBoard.web.board.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
public class ReplyDTO {

    private Long replyId;
    private Long boardId;
    private String registerId; // 작성자 회원 아이디, reply 테이블에 createdBy 에 저장 될 데이터.
    private String content;
    private Long replyGroup;
    private Long replyGroupOrder;
    private Long depth;
    private Long replyLike;
    private Boolean isRemove = false;

    // 클라이언트 측에서 부모댓글, 자식댓글에 대한 정보를 담당하는 부분.
    // 클라이언트 측에서 전달 된 다음 정보들을 바탕으로 replyService에서 댓글 CRUD 로직을 처리합니다.
    private Long parentReplyGroup;
    private Long parentReplyGroupOrder;
    private Long parentDepth;

    public ReplyDTO() {
    }

    // jpql 에서 특정 resultType로 반환하기 위해서 사용하는 생성자.
    // 수정하지 말자. (만일 수정해야 하는 경우, ReplyRepositoryImpl.getReplyList() 에 jpql 내부 생성자부분도 변경해주어야 한다.)
    public ReplyDTO(Long replyId, String registerId, String content, Long replyGroup,
                    Long replyGroupOrder, Long depth, Long replyLike, Boolean isRemove) {
        this.replyId = replyId;
        this.registerId = registerId;
        this.content = content;
        this.replyGroup = replyGroup;
        this.replyGroupOrder = replyGroupOrder;
        this.depth = depth;
        this.replyLike = replyLike;
        this.isRemove = isRemove;
    }

    @Builder
    public ReplyDTO(Long replyId, Long boardId, String registerId, String content, Long replyGroup,
                    Long replyGroupOrder, Long depth, Long replyLike,
                    Long parentReplyGroup, Long parentReplyGroupOrder, Long parentDepth, boolean isRemove) {
        this.replyId = replyId;
        this.boardId = boardId;
        this.registerId = registerId;
        this.content = content;
        this.replyGroup = replyGroup;
        this.replyGroupOrder = replyGroupOrder;
        this.depth = depth;
        this.replyLike = replyLike;
        this.parentReplyGroup = parentReplyGroup;
        this.parentReplyGroupOrder = parentReplyGroupOrder;
        this.parentDepth = parentDepth;
        this.isRemove = isRemove;
    }

}
