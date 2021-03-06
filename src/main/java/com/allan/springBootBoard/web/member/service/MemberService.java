package com.allan.springBootBoard.web.member.service;

import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;

public interface MemberService {

    public Long join(Member member);
    public Member findOneById(String memberId);
    public Long update(MemberDTO dto, String updatedBy);
    public void deleteAll();
}
