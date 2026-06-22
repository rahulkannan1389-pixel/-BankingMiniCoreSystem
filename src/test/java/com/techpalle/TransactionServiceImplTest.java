package com.techpalle;

import com.techpalle.dto.request.*;
import com.techpalle.dto.response.TransactionResponseDTO;
import com.techpalle.dto.response.TransferResponseDTO;
import com.techpalle.entity.Account;
import com.techpalle.entity.Transaction;
import com.techpalle.exception.InsufficientBalanceException;
import com.techpalle.exception.InvalidTransactionException;
import com.techpalle.exception.ResourceNotFoundException;
import com.techpalle.repository.AccountRepository;
import com.techpalle.repository.TransactionRepository;
import com.techpalle.serviceimpl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

	    @Mock
	    private AccountRepository accountRepository;

	    @Mock
	    private TransactionRepository transactionRepository;

	    @InjectMocks
	    private TransactionServiceImpl transactionService;

	    private Account account1;
	    private Account account2;

	    @BeforeEach
	    void setUp() {

	        account1 = new Account();
	        account1.setId(1L);
	        account1.setAccountNumber("ACC001");
	        account1.setBalance(new BigDecimal("5000"));

	        account2 = new Account();
	        account2.setId(2L);
	        account2.setAccountNumber("ACC002");
	        account2.setBalance(new BigDecimal("3000"));
	    }
	    
	    @Test
	    void testDepositSuccess() {

	        DepositRequestDTO dto = new DepositRequestDTO();
	        dto.setAccountId(1L);
	        dto.setAmount(new BigDecimal("1000"));

	        when(accountRepository.findByIdForUpdate(1L))
	                .thenReturn(Optional.of(account1));

	        when(transactionRepository.save(any(Transaction.class)))
	                .thenAnswer(i -> i.getArgument(0));

	        TransactionResponseDTO response = transactionService.deposit(dto);

	        assertNotNull(response);
	        assertEquals(new BigDecimal("6000"), account1.getBalance());

	        verify(accountRepository).findByIdForUpdate(1L);
	    }

	    @Test
	    void testWithdrawSuccess() {

	        WithdrawalRequestDTO dto = new WithdrawalRequestDTO();
	        dto.setAccountId(1L);
	        dto.setAmount(new BigDecimal("1000"));

	        when(accountRepository.findByIdForUpdate(1L))
	                .thenReturn(Optional.of(account1));

	        when(transactionRepository.save(any(Transaction.class)))
	                .thenAnswer(i -> i.getArgument(0));

	        TransactionResponseDTO response = transactionService.withdraw(dto);

	        assertNotNull(response);
	        assertEquals(new BigDecimal("4000"), account1.getBalance());
	    }

	    @Test
	    void testTransferSuccess() {

	        FundTransferRequestDTO dto = new FundTransferRequestDTO();
	        dto.setFromAccountId(1L);
	        dto.setToAccountId(2L);
	        dto.setAmount(new BigDecimal("1000"));

	        when(accountRepository.findByIdForUpdate(1L))
	                .thenReturn(Optional.of(account1));

	        when(accountRepository.findByIdForUpdate(2L))
	                .thenReturn(Optional.of(account2));

	        when(transactionRepository.save(any(Transaction.class)))
	                .thenAnswer(i -> i.getArgument(0));

	        TransferResponseDTO response = transactionService.transferFunds(dto);

	        assertNotNull(response);

	        assertEquals(new BigDecimal("4000"), account1.getBalance());
	        assertEquals(new BigDecimal("4000"), account2.getBalance());
	    }

	    @Test
	    void testWithdrawInsufficientBalance() {

	        WithdrawalRequestDTO dto = new WithdrawalRequestDTO();
	        dto.setAccountId(1L);
	        dto.setAmount(new BigDecimal("10000"));

	        when(accountRepository.findByIdForUpdate(1L))
	                .thenReturn(Optional.of(account1));

	        assertThrows(InsufficientBalanceException.class,
	                () -> transactionService.withdraw(dto));
	    }

	    @Test
	    void testAccountNotFound() {

	        DepositRequestDTO dto = new DepositRequestDTO();
	        dto.setAccountId(99L);
	        dto.setAmount(new BigDecimal("1000"));

	        when(accountRepository.findByIdForUpdate(99L))
	                .thenReturn(Optional.empty());

	        assertThrows(ResourceNotFoundException.class,
	                () -> transactionService.deposit(dto));
	    }
	    

         @Test
         void testInvalidAmount() {

         DepositRequestDTO dto = new DepositRequestDTO();
         dto.setAccountId(1L);
         dto.setAmount(new BigDecimal("-100"));

         assertThrows(InvalidTransactionException.class,
            () -> transactionService.deposit(dto));
         }

         @Test
         void testInactiveAccount() {

         account1.setActive(false);

         WithdrawalRequestDTO dto = new WithdrawalRequestDTO();
         dto.setAccountId(1L);
         dto.setAmount(new BigDecimal("100"));

         when(accountRepository.findByIdForUpdate(1L))
            .thenReturn(Optional.of(account1));

          assertThrows(InvalidTransactionException.class,
            () -> transactionService.withdraw(dto));
}




}
