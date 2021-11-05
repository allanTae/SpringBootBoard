package com.allan.springBootBoard.web.email.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {
    private String address;
    private String title;
    private String message;

    @Builder
    public MailDTO(String address, String title, String message) {
        this.address = address;
        this.title = title;
        this.message = message;
    }
}
