package com.techpalle.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.techpalle.entity.Transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseDTO {

    private Long transactionId;
    private String referenceNumber;

    private Long fromAccountId;
    private Long toAccountId;

    private String fromAccountNumber;
    private String toAccountNumber;

    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private TransactionStatus status;

    private String description;
}
