package com.allan.springBootBoard.web.board.repository;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.repository.mapper.ReplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.lang.Long;

//@Repository
@Slf4j
public class ReplyRepositoryTTImpl {


    /**
     * (수정 필요)
     * 삭제가 필요하다. 애초에 JPA 에서 지원하는 메소드이기 때문.
     * test용, 전체 댓글 삭제하기 위한 용도.
     */
//    @Override
//    public void deleteAll() {
//        replyMapper.deleteAll();
//    }


}
