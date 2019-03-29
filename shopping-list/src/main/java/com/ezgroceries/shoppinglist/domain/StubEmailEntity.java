package com.ezgroceries.shoppinglist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "STUB_EMAIL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {})
@EntityListeners(AuditingEntityListener.class)
public class StubEmailEntity extends BaseEntity {

    private String emailText;
    private String subject;
    private String toAddress;
    @CreatedDate
    private LocalDateTime createdDate;
}
