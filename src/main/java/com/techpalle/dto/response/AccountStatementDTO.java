package com.techpalle.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.techpalle.entity.Account.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatementDTO {
	
	    private Long accountId;
	    private String accountNumber;
	    private String customerName;

	    private AccountType accountType;

	    private BigDecimal openingBalance;
	    private BigDecimal closingBalance;

	    private LocalDate statementDate;

	    private List<TransactionHistoryDTO> transactions;


}
