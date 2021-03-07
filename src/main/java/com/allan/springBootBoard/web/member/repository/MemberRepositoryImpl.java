package com.allan.springBootBoard.web.member.repository;

import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import com.allan.springBootBoard.web.exception.NoDataException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.board.domain.model.MemberDTO;
import com.allan.springBootBoard.web.member.repository.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class MemberRepositoryImpl implements MemberRepository{

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberMapper memberMapper;

    /**
     * 회원 가입
     * @param member
     * @return member's pk
     */
    @Override
    public Long save(Member member){
        em.persist(member);
        return member.getMemberId();
    }

    /**
     * 단일 회원 조회
     * @param
     * @return
     */
    @Override
    public Member findOne(Long memberPk){

        return em.find(Member.class, memberPk);
    }

    /**
     * 회원 아이디로 단일 회원 조회
     * @param memberId
     * @return
     */
    @Override
    public Member findOneById(String memberId){
        try{
            Member member = em.createQuery("select m from Member m where m.id = :memberId", Member.class)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
            return member;
        } catch(NoResultException exception){
            return null;
        }

    }

    /**
     * 회원 정보(비밀번호, 주소) 수정
     * @param dto
     * @return member's pk
     */
    @Override
    public Long update(MemberDTO dto, String updatedBy) {
        Member findMember = em.find(Member.class, dto.getMemberId());
        findMember.changePassword(dto.getPassword(), updatedBy);
        findMember.changeAddress(dto.getAddress(), updatedBy);

        return findMember.getMemberId();
    }

    /**
     * spring seucrity 에서 회원 아이디로 비밀번호를 조회하기 위한 메소드
     * @param memberId
     * @return
     */
    @Override
    public Optional<Member> findByMemberId(String memberId){
//        try {
            Member member = em.createQuery("select m from Member m where m.id = :memberId", Member.class)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
            log.error("member is not null");
            return Optional.ofNullable(member);
//        } catch(EmptyResultDataAccessException e){
//            log.error("member is null");
//            return Optional.ofNullable(null);
//        }
    }

    /**
     * 테스트에서 database 를 초기화 시킬 메소드
     */
    @Override
    public void deleteAll() {
        memberMapper.deleteAll();
    }
}
