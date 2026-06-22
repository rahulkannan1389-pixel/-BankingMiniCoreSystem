package com.techpalle.dto.request;

import java.math.BigDecimal;

import com.techpalle.entity.Account.AccountType;
import com.techpalle.entity.Account.CurrencyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCreateRequestDTO {
	

	   @NotNull(message = "Customer ID is required")
	    private Long customerId;

	    @NotNull(message = "Account type is required")
	    private AccountType accountType;

	    @NotBlank(message = "IFSC is required")
	    private String ifscCode;

	    @NotNull(message = "Currency is required")
	    private CurrencyType currency;

	    @NotNull(message = "Initial balance required")
	    @Positive
	    private BigDecimal initialBalance;

	    private BigDecimal overdraftLimit;

	    private BigDecimal interestRate;


}
