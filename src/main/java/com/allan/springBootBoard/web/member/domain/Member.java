package com.allan.springBootBoard.web.member.domain;

import com.allan.springBootBoard.common.domain.BaseEntity;
import com.allan.springBootBoard.web.board.domain.Address;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(nullable = false, unique = true, length = 20)
    private String id;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long age;

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

    @PrePersist
    public void prePersist(){
        this.isEnable = true;
    }


    @Builder
    public Member(String id, String pwd, String name, Long age,
                  Gender gender, Address address, String createdBy, LocalDateTime createdDate,
                  String updatedBy, LocalDateTime updatedDate,
                  MemberRole role, String phoneNumber, String dateOfBirth) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public void changePassword(String password, String updatedBy){
        this.pwd = password;
        this.updatedBy = updatedBy;
        this.updatedDate = LocalDateTime.now();
    }

    public void changeAddress(Address address, String updatedBy){
        this.address = address;
        this.updatedBy = updatedBy;
        this.updatedDate = LocalDateTime.now();
    }

    public void changeIsEnable(boolean isEnable){
        this.isEnable = isEnable;
    }

}
