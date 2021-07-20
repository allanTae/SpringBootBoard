package com.allan.springBootBoard.web.service;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import com.allan.springBootBoard.web.board.service.BoardService;
import com.allan.springBootBoard.web.board.service.ReplyService;
import com.allan.springBootBoard.web.member.exception.SameIdUseException;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.service.MemberService;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    @BeforeEach
    public void setup(){
        replyService.deleteAll();
        boardService.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    public void 가입테스트() throws Exception {
        //given
        Address address = createAddress();

        Member member = createMember(address);

        //when
        memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(member.getMemberId()));
    }

    private Address createAddress() {
        return new Address("City", "Street", "123456", "", "");
    }

    private Member createMember(Address address) {
        return Member.builder()
                .id("testId")
                .pwd(passwordEncoder.encode("test"))
                .name("AllanTae")
                .age(29L)
                .gender(Gender.MAN)
                .address(address)
                .createdBy("testId")
                .createdDate(LocalDateTime.now())
                .role(MemberRole.USER)
                .phoneNumber("01012341234")
                .build();
    }

    @Test
    public void 중복회원_가입_테스트() throws Exception {
        //given
        Address address = createAddress();

        Member member1 = createMember(address);

        Member member2 = createMember(address);

        //when
        memberService.join(member1);

        //then
        SameIdUseException ex = assertThrows(SameIdUseException.class, () -> memberService.join(member2));
        assertEquals("이미 등록 된 회원 아이디입니다.", ex.getMessage());

    }

    @Test
    public void 회원_이름으로_조회() throws Exception {
        //given
        Address address = createAddress();
        Member member = createMember(address);

        //when
        em.persist(member);

        //then
        assertEquals(member.getId(), memberService.findOneById(member.getId()).getId());
    }

    @Test
    public void 회원정보_수정() throws Exception {
        //given
        Address addr = createAddress();
        Member member = createMember(addr);
        em.persist(member);

        //when
        MemberDTO dto = new MemberDTO();
        dto.setMemberId(member.getMemberId());
        dto.setPassword("updatedPs");
        memberService.update(dto, "관리자 아이디");

        //then
        assertEquals(member.getPwd(), memberRepository.findOne(member.getMemberId()).getPwd());

    }
}