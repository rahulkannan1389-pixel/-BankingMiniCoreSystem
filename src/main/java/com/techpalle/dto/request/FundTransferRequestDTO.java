package com.techpalle.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferRequestDTO {

    @NotNull
    private Long fromAccountId;

    @NotNull
    private Long toAccountId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Transfer mode is required")
     private TransferMode transferMode;

     public enum TransferMode {
           NEFT,
           RTGS,
           IMPS
       }
}
