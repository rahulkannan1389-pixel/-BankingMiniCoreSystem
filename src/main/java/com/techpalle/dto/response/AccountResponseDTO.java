package com.techpalle.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.techpalle.entity.Account.AccountStatus;
import com.techpalle.entity.Account.AccountType;
import com.techpalle.entity.Account.CurrencyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponseDTO {
	

	    private Long id;

	    private String accountNumber;
	    private Long customerId;
	    private String customerName;

	    private AccountType accountType;

	    private BigDecimal balance;
	    private BigDecimal overdraftLimit;

	    private String ifscCode;

	    private CurrencyType currency;

	    private LocalDate accountOpenedDate;
	    private LocalDate lastTransactionDate;

	    private AccountStatus accountStatus;

	    private BigDecimal interestRate;

	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

}
