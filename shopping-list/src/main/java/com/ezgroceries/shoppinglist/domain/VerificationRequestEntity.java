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
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "VERIFICATION_REQUEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, of = {})
@EntityListeners(AuditingEntityListener.class)
public class VerificationRequestEntity extends BaseEntity {

    private UUID userId;
    private String verificationCode;
    private Long validity;
    @CreatedDate
    private Date createdDate;
}
