package com.ezgroceries.shoppinglist.contract.shoppinglist;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class StubEmailResource {

    private String emailText;
    private String subject;
    private String toAddress;
    private LocalDateTime createdDate;
}
