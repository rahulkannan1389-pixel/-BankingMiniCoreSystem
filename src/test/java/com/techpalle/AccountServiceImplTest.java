package com.techpalle;


import com.techpalle.dto.request.AccountCreateRequestDTO;
import com.techpalle.dto.response.AccountResponseDTO;
import com.techpalle.entity.Account;
import com.techpalle.entity.Customer;
import com.techpalle.exception.ResourceNotFoundException;
import com.techpalle.repository.AccountRepository;
import com.techpalle.repository.CustomerRepository;
import com.techpalle.serviceimpl.AccountServiceImpl;
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
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");

        account = new Account();
        account.setId(1L);
        account.setAccountNumber("ACC001");
        account.setBalance(new BigDecimal("5000"));
        account.setCustomer(customer);
    }

    @Test
    void testCreateAccount() {

        AccountCreateRequestDTO dto = new AccountCreateRequestDTO();
        dto.setCustomerId(1L);
        dto.setInitialBalance(new BigDecimal("5000"));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        AccountResponseDTO response = accountService.createAccount(dto);

        assertNotNull(response);
        assertEquals("ACC001", response.getAccountNumber());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testCreateAccountCustomerNotFound() {

        AccountCreateRequestDTO dto = new AccountCreateRequestDTO();
        dto.setCustomerId(99L);

        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.createAccount(dto));
    }

    @Test
    void testGetAccountById() {

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        AccountResponseDTO response = accountService.getAccountById(1L);

        assertNotNull(response);
        assertEquals("ACC001", response.getAccountNumber());
    }

    @Test
    void testCloseAccount() {

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        accountService.closeAccount(1L);

        assertFalse(account.isActive());

        verify(accountRepository).save(any(Account.class));
    }
}
