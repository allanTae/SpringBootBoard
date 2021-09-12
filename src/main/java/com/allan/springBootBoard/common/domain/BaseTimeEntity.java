package com.allan.springBootBoard.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @Column(nullable = false, updatable = false)
    @CreatedDate
    public LocalDateTime createdDate;

    @LastModifiedDate
    public LocalDateTime updatedDate;
}
