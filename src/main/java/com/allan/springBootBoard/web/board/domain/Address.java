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
    @Column(nullable = false)
    private final String city;

    @Column(nullable = false)
    private final String street;

    @Column(nullable = false)
    private final String zipcode;
}
