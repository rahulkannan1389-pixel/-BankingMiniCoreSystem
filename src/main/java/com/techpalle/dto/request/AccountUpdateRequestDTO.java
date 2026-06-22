package com.techpalle.dto.request;

import java.math.BigDecimal;
import com.techpalle.entity.Account.AccountStatus;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateRequestDTO {

  private AccountStatus accountStatus;

    @PositiveOrZero
    private BigDecimal overdraftLimit;

    @PositiveOrZero
    private BigDecimal interestRate;

	

}
