package com.study.shop.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class } )
@Getter
public abstract class BaseEntity extends BaseTimeEntity {
    // 등록자, 수정자, 등록일, 수정일 포함

    @CreatedBy
    @Column(name = "createdBy", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modifiedBy")
    private String modifiedBy;
}
