package com.cookpadidn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditableEntity implements Serializable {
    @Id
    private UUID id;
    @CreatedDate
    LocalDateTime createDate;
    @LastModifiedDate
    LocalDateTime lastModifiedDate;

    @PrePersist
    protected void prePersist() {
        this.id = UUID.randomUUID();
        if (this.createDate == null) createDate = LocalDateTime.now();
        if (this.lastModifiedDate == null) lastModifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdated(){
        this.lastModifiedDate = LocalDateTime.now();
    }

    @PreRemove
    protected void preRemove(){
        this.lastModifiedDate = LocalDateTime.now();
    }

}
