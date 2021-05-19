package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.mapper.ReplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class ReplyRepositoryImpl implements ReplyRepository{

    @PersistenceContext
    EntityManager em;

    @Autowired
    ReplyMapper replyMapper;

    /**
     * 게시물로 댓글 조회.
     * @param
     * @return List<Reply>
     */
    @Override
    public List<ReplyDTO> getReplyList(Long boardId) {
        String qlString = "select new com.allan.springBootBoard.web.board.domain.model.ReplyDTO(r.replyId, r.createdBy, r.content, r.replyGroup, r.replyGroupOrder, " +
                "r.depth, r.replyLike)" +
                " from Reply r join r.board b on b.boardId = :boardId order by  " +
                "r.replyGroup asc, r.replyGroupOrder asc";
        List<ReplyDTO> list = em.createQuery(qlString, ReplyDTO.class)
                .setParameter("boardId", boardId)
                .getResultList();

        return list;
    }

    /**
     * 댓글 등록 및 수정.
     * @param reply
     * @return 댓글 아이디.
     */
    @Override
    public Long insertReply(Reply reply) {
        em.persist(reply);
        return reply.getReplyId();
    }

    /**
     * 댓글 pk 값을 설정하기 위해 reply 테이블에 pk 값중 가장 큰 값을 반환.
     * @param boardId
     * @return
     */
    @Override
    public Long getMaxReplyId() {

        List<Long> idList = em.createQuery("select max(r.replyId) from Reply r", Long.class)
                .getResultList();

        Long maxReplyId = (idList.get(0) == null)? 1L:idList.get(0)+1;
        return maxReplyId;
    }


    /**
     * (자식 댓글 등록시에 사용하는 메소드)
     * 같은 그룹 번호에 속하는 총 댓글 수를 반환.
     * @param dto
     * @return
     */
    @Override
    public Long getReplyListCnt(ReplyDTO dto) {

        Long cnt = em.createQuery("select count(r) from Reply r join r.board b on b.boardId = :board_Id and r.replyGroup = :parentReplyGroup", Long.class)
                .setParameter("board_Id", dto.getBoardId())
                .setParameter("parentReplyGroup", dto.getParentReplyGroup())
                .getSingleResult();

        return cnt;
    }

    /**
     * (자식 댓글 등록시에 사용하는 메소드)
     * 자식 댓글을 등록할때, 자식 댓글들 사이에 등록하는 경우
     * 기존에 자식 댓글들에 순서를 모두 한차례씩 밀어내기 위해 시작 순서를 반환.
     * @param dto
     * @return
     */
    @Override
    public Long getGroupOrder(ReplyDTO dto) {

        Long groupOrder = em.createQuery("select min(r.replyGroupOrder) from Reply r join r.board b on b.boardId = :boardId " +
                "and r.replyGroup = :parentReplyGroup and r.replyGroupOrder > :parentReplyGroupOrder and r.depth <= :parentDepth", Long.class)
                .setParameter("boardId", dto.getBoardId())
                .setParameter("parentReplyGroup", dto.getParentReplyGroup())
                .setParameter("parentReplyGroupOrder", dto.getParentReplyGroupOrder())
                .setParameter("parentDepth", dto.getParentDepth())
                .getSingleResult();

        return groupOrder;
    }

    /**
     * (자식 댓글 등록시에 사용하는 메소드)
     * 자식댓글의 삽입 할때, 그룹내 순서를 조정하는 메소드
     * @param dto
     * @return
     */
    @Override
    public void updateGroupOrder(ReplyDTO dto) {
        log.info("updateGroupOrder call!!!");
        em.createQuery("update Reply r set r.replyGroupOrder = r.replyGroupOrder +1, r.updatedBy = :updatedBy, r.updatedDate = :updatedDate " +
                "where r.board.boardId = :boardId and r.replyGroup = :parentReplyGroup and r.replyGroupOrder >= :parentReplyGroupOrder")
                .setParameter("updatedBy", dto.getUpdatedBy())
                .setParameter("updatedDate", dto.getUpdatedDate())
                .setParameter("boardId", dto.getBoardId())
                .setParameter("parentReplyGroup", dto.getParentReplyGroup())
                .setParameter("parentReplyGroupOrder", dto.getParentReplyGroupOrder())
                .executeUpdate();

    }

    /**
     * 댓글을 수정하는 메소드
     * @param dto
     * @return
     */
    @Override
    public Long updateReply(ReplyDTO dto) {
        Reply reply = em.find(Reply.class, dto.getReplyId());
        reply.changeContent(dto.getContent());
        reply.changeUpdateInfo(dto.getUpdatedBy(), dto.getUpdatedDate());
        return reply.getReplyId();
    }

    /**
     * 댓글 삭제하는 메소드
     * @param replyId
     * @return
     */
    @Override
    public Long deleteReply(Long replyId, Long boardId) {
        Reply reply = em.find(Reply.class, replyId);
        Board board = em.find(Board.class, boardId);
        board.getReplyList().remove(reply);
        em.remove(reply);
        return replyId;
    }

    /**
     * test용, 전체 댓글 삭제하기 위한 용도.
     */
    @Override
    public void deleteAll() {
        replyMapper.deleteAll();
    }


}
