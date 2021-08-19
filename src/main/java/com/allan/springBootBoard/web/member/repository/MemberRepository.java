package com.allan.springBootBoard.web.member.repository;

import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findById(String id);
    public Optional<Member> findByMemberId(Long memberId);

//    public Long save(Member member);
//    public Member findOne(Long memberPk);
//    public Member findOneById(String memberId);
//    public Long update(MemberDTO dto, String updatedBy);
//    public Optional<Member> findByMemberId(String memberId);
//    public void deleteAll();
}
