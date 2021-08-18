package com.allan.springBootBoard.web.member.service;

import com.allan.springBootBoard.web.member.exception.SameIdUseException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    @Override
    public Long join(Member member){
        if(validateId(member)){
            memberRepository.save(member);
        }
        return member.getMemberId();
    }

    private boolean validateId(Member member) {
        Member findMember = memberRepository.findOneById(member.getId());
        if(findMember == null){
            return true;
        }else{
            throw new SameIdUseException("이미 등록 된 회원 아이디입니다.");
        }
    }

    @Override
    public Member findOneById(String memberId) {
        return memberRepository.findOneById(memberId);
    }

    @Transactional
    @Override
    public Long update(MemberDTO dto, String updatedBy) {
        return memberRepository.update(dto, updatedBy);
    }

    @Transactional
    @Override
    public void deleteAll() {
        memberRepository.deleteAll();
    }
}
