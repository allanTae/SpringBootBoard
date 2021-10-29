package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryJPQL {


    /**
     * 게시물로 댓글 조회.
     * @param
     * @return List<Reply>
     *     댓글을 조회하는 과정중에 회원의 아이디가 아닌 이름이 필요했기 때문에 Member 테이블과 조인을 추가함.
     */

    @Query("select new com.allan.springBootBoard.web.board.domain.model.ReplyDTO(r.replyId, r.createdBy, r.content, r.replyGroup, r.replyGroupOrder, " +
            "r.depth, r.replyLike, r.isRemove, m.name, m.authId, m.role)" +
            " from Reply r join r.board b on b.boardId = :boardId " +
            " join Member m on r.createdBy = m.authId order by  " +
            "r.replyGroup asc, r.replyGroupOrder asc")
    public List<ReplyDTO> getReplyList(@Param("boardId") Long boardId);

    /**
     * (자식 댓글 등록시에 사용하는 메소드)
     * 같은 그룹 번호에 속하는 총 댓글 수를 반환.
     * @param boardId
     * @param parentReplyGroup
     * @return
     */
    @Query("select count(r) from Reply r join r.board b on b.boardId = :boardId and r.replyGroup = :parentReplyGroup")
    public Long getReplyListCnt(@Param("boardId")Long boardId, @Param("parentReplyGroup") Long parentReplyGroup);

    /**
     * (자식 댓글 등록시에 사용하는 메소드)
     * 자식 댓글을 등록할때, 자식 댓글들 사이에 등록하는 경우
     * 기존에 자식 댓글들에 순서를 모두 한차례씩 밀어내기 위해 시작 순서를 반환.
     * @param boardId
     * @param parentReplyGroup
     * @param parentReplyGroupOrder
     * @param parentDepth
     * @return
     */
    @Query("select min(r.replyGroupOrder) from Reply r join r.board b on b.boardId = :boardId " +
            "and r.replyGroup = :parentReplyGroup and r.replyGroupOrder > :parentReplyGroupOrder and r.depth <= :parentDepth")
    public Long getGroupOrder(@Param("boardId") Long boardId,
                              @Param("parentReplyGroup") Long parentReplyGroup,
                              @Param("parentReplyGroupOrder") Long parentReplyGroupOrder,
                              @Param("parentDepth") Long parentDepth);

    /**
     * (자식 댓글 등록시에 사용하는 메소드)
     * 자식댓글의 삽입 할때, 그룹내 순서를 조정하는 메소드
     * @param updatedBy
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update Reply r set r.replyGroupOrder = r.replyGroupOrder +1, r.updatedBy = :updatedBy, r.updatedDate = :updatedDate " +
            "where r.board.boardId = :boardId and r.replyGroup = :parentReplyGroup and r.replyGroupOrder >= :parentReplyGroupOrder")
    public void updateGroupOrder(@Param("updatedBy") String updatedBy,
                                 @Param("updatedDate") LocalDateTime updatedDate,
                                 @Param("boardId") Long boardId,
                                 @Param("parentReplyGroup") Long parentReplyGroup,
                                 @Param("parentReplyGroupOrder") Long parentReplyGroupOrder);

}
