package com.techpalle.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.techpalle.entity.Transaction.TransactionStatus;
import com.techpalle.entity.Transaction.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionHistoryDTO {

    private Long id;

    private TransactionType transactionType;

    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;

    private LocalDateTime transactionDate;

    private String referenceNumber;

    private String description;

    private TransactionStatus transactionStatus;
}
