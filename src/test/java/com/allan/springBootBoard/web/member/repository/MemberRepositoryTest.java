package com.allan.springBootBoard.web.member.repository;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureMybatis
public class MemberRepositoryTest {

    private String TEST_MEMBER_AUTH_ID = "TEST";

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    private void setUp(){
        memberRepository.deleteAll();
    }

    @Test
    public void 아이디로_회원조회_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember();
        testEntityManager.persist(TEST_MEMBER);

        //when
        boolean present = memberRepository.findByAuthId(TEST_MEMBER_AUTH_ID).isPresent();

        //then
        assertTrue(present);
    }

    @Test
    public void Member_식별자로_회원조회_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember();
        testEntityManager.persist(TEST_MEMBER);

        //when
        boolean present = memberRepository.findByMemberId(TEST_MEMBER.getMemberId()).isPresent();

        //then
        assertTrue(present);
    }

    private Member createMember() {
        return Member.builder()
                .authId(TEST_MEMBER_AUTH_ID)
                .pwd("TEST")
                .name("TESTER")
                .age(10l)
                .address(new Address("", "", "", "", ""))
                .createdBy("TESTER")
                .createdDate(LocalDateTime.now())
                .gender(Gender.MAN)
                .phoneNumber("01022223333")
                .dateOfBirth("19221102")
                .role(MemberRole.USER)
                .build();
    }
}
