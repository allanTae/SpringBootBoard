package com.allan.springBootBoard.web.member.service;

import com.allan.springBootBoard.security.user.exception.UserNotFoundException;
import com.allan.springBootBoard.web.member.exception.SameIdUseException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    @NonNull
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
        boolean isValidated = memberRepository.findById(member.getId()).isPresent();
        if(!isValidated){
            return true;
        }else{
            throw new SameIdUseException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public Member findById(String id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserNotFoundException("회원이 존재하지 않습니다."));
    }

    @Override
    public Member findByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new UserNotFoundException("회원이 존재하지 않습니다."));
    }

    @Transactional
    @Override
    public Long updateMemberInfo(MemberDTO dto, String updatedBy) {
        Member findMember =  memberRepository.findByMemberId(dto.getMemberId()).orElseThrow(() -> new UserNotFoundException("회원이 존재하지 않습니다."));
        findMember.changeAddress(dto.getAddress(), updatedBy);
        findMember.changePassword(dto.getPassword(), updatedBy);
        return findMember.getMemberId();
    }

    @Transactional
    @Override
    public void deleteAll() {
        memberRepository.deleteAll();
    }
}
