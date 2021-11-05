package com.allan.springBootBoard.web.member.domain;

import com.allan.springBootBoard.common.domain.BaseEntity;
import com.allan.springBootBoard.common.domain.BaseTimeEntity;
import com.allan.springBootBoard.security.user.domain.UserDetailsVO;
import com.allan.springBootBoard.web.board.domain.Address;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(nullable = false, unique = true, length = 20)
    private String authId;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long age;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean isEnable;

    @Column(nullable = false, name="DATE_OF_BIRTH")
    private String dateOfBirth;

    @Column(nullable = false, updatable = false)
    private String createdBy;

    private String updatedBy;

    @Column(nullable = false)
    private String nickname;

    @Transient
    private Boolean isSystemControll = false; // system 여부 확인 토글.

    /**
     * 회원 가입 처리는 회원아이디가 생성되지 않은 상태이므로 spring Jpa Auditing 으로는 불가하기에
     * 따로 등록자, 수정자를 엔티티에서 순수 JPA를 통하여 수정하도록 처리하였습니다.
     * 또한 등록자와 초기 수정자는 system 으로 기재하도록 처리 되었습니다.
     *
     * 초기 가입시, 가입유형에 따라 nickname 을 소셜가입시 name 으로, 서비스가입시 authId 로 설정하도록 하였습니다.
     */
    @PrePersist
    public void prePersist(){
        this.isEnable = true;
        createdBy = "system";
        updatedBy = "system";
        if(role.getJoinType().getType().equals(JoinType.NORMAL.getType())){
            this.nickname = authId;
        }else{
            this.nickname= name;
        }
    }

    /**
     * 이후 수정 결과는 기존에 다른 엔티티들과 동일하게 spring security 에 로그인 한 아이디를
     * 통해 수정하도록 처리하였습니다.
     * (spring security 에 의존적이기 때문에 리팩토링이 필요합니다.)
     * '비밀번호 찾기' 기능 같이 시스템에서 값을 변경해야하는 경우 '수정자'가 system 으로 나타내도록 처리합니다.
     */
    @PreUpdate
    public void preUpdate() {
        if(!isSystemControll){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(null == authentication || !authentication.isAuthenticated()){
            }else{
                UserDetailsVO user = (UserDetailsVO) authentication.getPrincipal();
                updatedBy = user.getUsername();
            }
        }else{
            updatedBy = "system";
            isSystemControll = false; // 굳이 변경하지 않아도 상관없는가.? 애초에 매번 초기값으로 false 로 설정 되도록 할 수는 없는가.?
        }
    }

    @Builder
    public Member(String authId, String pwd, String name, Long age,
                  Gender gender, Address address,
                  MemberRole role, String phoneNumber, String dateOfBirth, String email, String picture) {
        this.authId = authId;
        this.pwd = pwd;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.picture = picture;
    }

    public void changePassword(String password){
        this.pwd = password;
    }

    public void changeAddress(Address address){
        this.address = address;
    }

    public void changeIsEnable(boolean isEnable){
        this.isEnable = isEnable;
    }

    public void changeNickname(String nickname) {this.nickname = nickname;}

    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return role.getKey();
    }

    // 임시 비밀번호 생성 메소드.
    public String getTempPwd(){
        this.isSystemControll = true;
        char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String tempPwd = "";
        int idx = 0;
        for(int i=0; i<10; i++){
            idx = (int) (charSet.length * Math.random());
            tempPwd += charSet[idx];
        }
        return tempPwd;
    }
}
