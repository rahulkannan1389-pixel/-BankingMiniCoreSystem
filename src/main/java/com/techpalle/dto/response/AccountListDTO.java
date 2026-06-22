package com.techpalle.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.techpalle.entity.Account.AccountStatus;
import com.techpalle.entity.Account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountListDTO {
	
    private Long id;
    private String accountNumber;

    private AccountType accountType;

    private BigDecimal balance;

    private AccountStatus accountStatus;

    private LocalDate lastTransactionDate;


}
