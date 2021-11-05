package com.allan.springBootBoard.web.member.service;

import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;

public interface MemberService {

    /**
     * 회원 가입 처리하는 메소드입니다.
     * 클라이언트로부터 전달 받은 form 정보를 토대로 Member 엔티티를 저장합니다.
     * @param form
     * @param dateOfBirth
     * @return
     */
    public Long join(MemberForm form, String dateOfBirth);

    /**
     * 회원 아이디로 회원을 조회하는 메소드입니다.
     * @param authId
     * @return
     */
    public Member findByAuthId(String authId);

    /**
     * 회원 정보를 수정하는 메소드입니다.
     * @param memberDTO
     * @param updatedBy
     * @return
     */
    public Long updateMemberInfo(MemberDTO memberDTO, String updatedBy);

    /**
     * 입력받은 회원 이름, 아이디 정보로 회원가입 여부를 확인하는 메소드입니다.
     * @param memberName
     * @param authId
     * @return
     */
    public boolean isJoined(String memberName, String authId);

    /**
     * 회원의 비밀번호를 임시 비밀번호로 변경 후, 회원의 이메일로 임시 비밀번호를 발송하는 메소드입니다.
     * @param memberName
     * @param authId
     * @return
     */
    public void changePwdAndSendEmail(String memberName, String authId);
}
