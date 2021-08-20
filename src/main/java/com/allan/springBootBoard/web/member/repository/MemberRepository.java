package com.allan.springBootBoard.web.member.repository;

import com.allan.springBootBoard.web.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findById(String id);
    public Optional<Member> findByMemberId(Long memberId);

}
