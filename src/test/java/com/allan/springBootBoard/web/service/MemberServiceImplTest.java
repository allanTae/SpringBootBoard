package com.allan.springBootBoard.web.service;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.service.MemberService;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
class MemberServiceImplTest {

    String TEST_MEMBER_ID = "testId";
    Long TEST_MEMBER_ENTITY_ID = 1l;

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
//        memberService = new MemberServiceImpl(memberRepository);
    }

    @Test
    public void 가입_테스트() throws Exception {
        //given
        Address TEST_ADDR =  createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);
        given(memberRepository.save(any(Member.class)))
                .willReturn(TEST_MEMBER);

        //when
//        memberService.join(TEST_MEMBER);

        //then
        verify(memberRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 중복회원가입_테스트() throws Exception {
        //given
        Address TEST_ADDR = createAddress();
        Member TEST_MEMBER1 = createMember(TEST_ADDR, TEST_MEMBER_ID);
        given(memberRepository.findById(any(String.class)))
                .willReturn(Optional.of(TEST_MEMBER1));

        // when, then
//        assertThrows(SameIdUseException.class, () -> memberService.join(TEST_MEMBER1));
    }

    @Test
    public void 회원아이디로_조회_테스트() throws Exception {
        //given
        Address TEST_ADDR = createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);
        given(memberRepository.findById(any((String.class))))
                .willReturn(Optional.of(TEST_MEMBER));

        //when
        memberService.findByAuthId(TEST_MEMBER_ID);

        //then
        assertThat(TEST_MEMBER.getId(), is(TEST_MEMBER_ID));
    }

    @Test
    public void 회원정보수정_테스트() throws Exception {
        //given
        Address TEST_ADDR = createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);

        MemberDTO TEST_MEM_DTO = new MemberDTO();
        TEST_MEM_DTO.setMemberId(TEST_MEMBER_ENTITY_ID);
        TEST_MEM_DTO.setAddress(new Address("updateCity", "updateStreet", "", "", ""));
        TEST_MEM_DTO.setPassword("updatePassword");

        given(memberRepository.findByMemberId(TEST_MEMBER_ENTITY_ID))
                .willReturn(Optional.of(TEST_MEMBER));

        //when
        memberService.updateMemberInfo(TEST_MEM_DTO, "updatedBy");

        //then
        verify(memberRepository, atLeastOnce()).findByMemberId(any(Long.class));
        assertThat(TEST_MEMBER.getAddress().getJibunAddress(), is("updateCity"));
        assertThat(TEST_MEMBER.getPwd(), is("updatePassword"));
    }

    private Address createAddress() {
        return new Address("City", "Street", "123456", "", "");
    }

    private Member createMember(Address address, String id) {
        Member member = Member.builder()
                .id(id)
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

        ReflectionTestUtils.setField(member, "memberId", TEST_MEMBER_ENTITY_ID);

        return member;
    }

}