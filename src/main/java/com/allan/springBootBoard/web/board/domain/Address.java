package com.allan.springBootBoard.web.board.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class Address {
    @Column(nullable = false, name="JIBUN_ADDRESS")
    private final String jibunAddress;

    @Column(nullable = false, name="ROAD_ADDRESS")
    private final String roadAddress;

    @Column(nullable = false)
    private final String postcode;

    @Column(nullable = false, name ="DETAIL_ADDRESS")
    private final String detailAddress;

    @Column(name="EXTRA_ADDRESS")
    private final String extraAddress;
}
