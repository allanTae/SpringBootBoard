package com.allan.springBootBoard.web.member.service;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.email.service.EmailService;
import com.allan.springBootBoard.web.member.domain.model.MemberDTO;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import com.allan.springBootBoard.web.member.domain.model.MemberForm;
import com.allan.springBootBoard.web.member.exception.SameIdUseException;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
class MemberServiceTest {

    String TEST_MEMBER_ID = "testId";
    Long TEST_MEMBER_ENTITY_ID = 1l;

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    EmailService emailService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(memberRepository, passwordEncoder, emailService);
    }

    @Test
    public void 가입_테스트() throws Exception {
        //given
        Address TEST_ADDR =  createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);
        MemberForm form = new MemberForm();
        form.setAuthId("TEST");
        form.setPwd("TEST");
        form.setDetailAddress("TEST");
        form.setExtraAddress("TEST");
        form.setRoadAddress("TEST");
        form.setJibunAddress("TEST");
        form.setPostcode("TEST");
        form.setGender("1");
        form.setPhone("22222222");
        form.setName("TESTER");
        form.setAge(10l);

        given(memberRepository.save(any(Member.class)))
                .willReturn(TEST_MEMBER);

        //when
        memberService.join(form, "19931221");

        //then
        verify(passwordEncoder, atLeastOnce()).encode(any());
        verify(memberRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 중복회원가입_테스트() throws Exception {
        //given
        Address TEST_ADDR =  createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);
        MemberForm form = new MemberForm();
        form.setAuthId("TESTER");
        form.setPwd("TEST");
        form.setDetailAddress("TEST");
        form.setExtraAddress("TEST");
        form.setRoadAddress("TEST");
        form.setJibunAddress("TEST");
        form.setPostcode("TEST");
        form.setGender("1");
        form.setPhone("22222222");
        form.setName("TESTER");
        form.setAge(10l);

        given(memberRepository.findByAuthId(any(String.class)))
                .willReturn(Optional.of(TEST_MEMBER));

        // when, then
        assertThrows(SameIdUseException.class, () -> memberService.join(form, "19220525"));
    }

    @Test
    public void 회원아이디로_조회_테스트() throws Exception {
        //given
        Address TEST_ADDR = createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);
        given(memberRepository.findByAuthId(any((String.class))))
                .willReturn(Optional.of(TEST_MEMBER));

        //when
        memberService.findByAuthId(TEST_MEMBER_ID);

        //then
        verify(memberRepository, atLeastOnce()).findByAuthId(any(String.class));
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

    @Test
    public void 아이디_이메일로_회원가입확인_테스트() throws Exception {
        //given
        Address TEST_ADDR = createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);
        given(memberRepository.findByAuthId(any()))
                .willReturn(Optional.of(TEST_MEMBER));

        //when
        memberService.isJoined(TEST_MEMBER.getName(),TEST_MEMBER.getAuthId());

        //then
        verify(memberRepository, atLeastOnce()).findByAuthId(any());
    }

    @Test
    public void 비밀번호변경_이메일발송_테스트() throws Exception {
        //given
        String TEST_ENCODE_PWD = "incodePwd";
        Address TEST_ADDR = createAddress();
        Member TEST_MEMBER = createMember(TEST_ADDR, TEST_MEMBER_ID);
        given(memberRepository.findByAuthId(any()))
                .willReturn(Optional.of(TEST_MEMBER));
        given(passwordEncoder.encode(any()))
                .willReturn(TEST_ENCODE_PWD);

        //when
        memberService.changePwdAndSendEmail(TEST_MEMBER.getName(), TEST_MEMBER.getAuthId());

        //then
        verify(memberRepository, atLeastOnce()).findByAuthId(any());
        verify(emailService, atLeastOnce()).mailSend(any());
        assertThat(TEST_MEMBER.getPwd(), is(TEST_ENCODE_PWD));
    }

    private Address createAddress() {
        return new Address("City", "Street", "123456", "", "");
    }

    private Member createMember(Address address, String id) {
        Member member = Member.builder()
                .authId(id)
                .pwd(passwordEncoder.encode("test"))
                .name("AllanTae")
                .age(29L)
                .gender(Gender.MAN)
                .address(address)
                .role(MemberRole.USER)
                .phoneNumber("01012341234")
                .build();

        ReflectionTestUtils.setField(member, "memberId", TEST_MEMBER_ENTITY_ID);

        return member;
    }

}