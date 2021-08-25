package com.allan.springBootBoard.web.member.service;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.error.code.ErrorCode;
import com.allan.springBootBoard.web.error.exception.MemberNotFoundException;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.exception.SameIdUseException;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    @NonNull
    MemberRepository memberRepository;

    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원 가입 처리하는 메소드입니다.
     * 클라이언트로부터 전달 받은 form 정보를 토대로 Member 엔티티를 저장합니다.
     * @param form
     * @param dateOfBirth
     * @return
     */
    @Transactional
    @Override
    public Long join(MemberForm form, String dateOfBirth){

        Member member = Member.builder()
                .id(form.getMemberId())
                .pwd(passwordEncoder.encode(form.getPwd()))
                .role(MemberRole.USER)
                .name(form.getName())
                .address(new Address(form.getJibunAddress(), form.getRoadAddress(), form.getPostcode(), form.getDetailAddress(), form.getExtraAddress()))
                .age(form.getAge())
                .phoneNumber(form.getPhone())
                .gender(Gender.valueOf(Integer.parseInt(form.getGender()))) // 폼에서 전달 된 String 값을 int로 변환하기 위함.
                .createdBy(form.getMemberId())
                .createdDate(LocalDateTime.now())
                .dateOfBirth(dateOfBirth)
                .build();

        if(validateId(member)){
            memberRepository.save(member);
        }
        return member.getMemberId();
    }

    /** 회원가입시, 아이디 존재 유무를 확인하는 메소드 입니다.
     * 서비스 단에서 중복 회원가입 여부를 확인 할 때 사용합니다.
     * @param member
     * @return
     */
    private boolean validateId(Member member) {
        boolean isValidated = memberRepository.findById(member.getId()).isPresent();
        if(!isValidated){
            return true;
        }else{
            throw new SameIdUseException("이미 존재하는 회원 아이디 입니다.", ErrorCode.Member_ID_DUPLICATION);
        }
    }

    /**
     * 회원 아이디로 회원을 조회하는 메소드입니다.
     * @param authId
     * @return
     */
    @Override
    public Member findByAuthId(String authId) {
        return memberRepository.findById(authId).orElseThrow(() -> new MemberNotFoundException("해당 Member 엔티티가 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
    }

    /**
     * 회원 정보를 수정하는 메소드입니다.
     * @param memberDTO
     * @param updatedBy
     * @return
     */
    @Transactional
    @Override
    public Long updateMemberInfo(MemberDTO memberDTO, String updatedBy) {
        Member findMember =  memberRepository.findByMemberId(memberDTO.getMemberId()).orElseThrow(() -> new MemberNotFoundException("해당 Member 엔티티가 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
        findMember.changeAddress(memberDTO.getAddress(), updatedBy);
        findMember.changePassword(memberDTO.getPassword(), updatedBy);
        return findMember.getMemberId();
    }

}
