package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryJPQL {

    @PersistenceContext
    EntityManager em;

    /**
     * 댓글 pk 값을 설정하기 위해 reply 테이블에 pk 값중 가장 큰 값을 반환.
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
     * (수정 필요)
     * (depreciated 예정)
     * 댓글 완전 삭제하는 메소드
     * 수정해야 하는 메소드 Reply 뿐만 아니라, Board 에도 의존하고 있다.
     * 차라리 Service 단에서 각 Repository에서 가져와서 사용하는 형태로 수정이 필요하다.
     * (reply 의 삭제 처리는 updateReply() 를 통해서 삭제 플래그를 변경하는 형태로 변경되었기에, 이 기능은 depreciated 해야 한다.)
     * @param replyId
     * @return
     * @deprecated reply 엔티티를 완전 삭제하기 보다 삭제플래그를 도입하여 삭제 플래그만 변경하는 것으로 변경하였다.
     */
    @Override
    public Long deleteReply(Long replyId, Long boardId) {
        Reply reply = em.find(Reply.class, replyId);
        Board board = em.find(Board.class, boardId);
        board.getReplyList().remove(reply);
        em.remove(reply);
        return replyId;
    }

}
