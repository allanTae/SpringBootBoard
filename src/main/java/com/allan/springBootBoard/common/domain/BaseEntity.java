package com.allan.springBootBoard.common.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity {

    @Column(nullable = false)
    public String createdBy;

    @Column(nullable = false)
    public LocalDateTime createdDate;
    public String updatedBy;
    public LocalDateTime updatedDate;

}
