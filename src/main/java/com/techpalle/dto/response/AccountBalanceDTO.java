package com.techpalle.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBalanceDTO {

	   private Long accountId;
	    private String accountNumber;

	    private BigDecimal balance;
	    private BigDecimal overdraftLimit;
	    private BigDecimal availableBalance;
	    private LocalDateTime lastUpdated;


}
