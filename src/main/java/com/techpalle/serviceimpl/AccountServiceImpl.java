package com.techpalle.serviceimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techpalle.dto.request.AccountCreateRequestDTO;
import com.techpalle.dto.request.AccountUpdateRequestDTO;
import com.techpalle.dto.response.AccountResponseDTO;
import com.techpalle.entity.Account;
import com.techpalle.entity.Customer;
import com.techpalle.exception.AccountOperationException;
import com.techpalle.exception.ResourceNotFoundException;
import com.techpalle.repository.AccountRepository;
import com.techpalle.repository.CustomerRepository;
import com.techpalle.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {
	

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    //  CREATE ACCOUNT
    @Override
    public AccountResponseDTO createAccount(AccountCreateRequestDTO dto) {

        log.info("Entering createAccount() for customerId={}", dto.getCustomerId());

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> {
                    log.error("Customer not found for account creation, id={}", dto.getCustomerId());
                    return new ResourceNotFoundException("CUSTOMER_NOT_FOUND", "Customer not found");
                });

        try {
            Account account = new Account();

            account.setAccountNumber(generateAccountNumber());
            account.setCustomer(customer);
            account.setAccountType(dto.getAccountType());
            account.setBalance(dto.getInitialBalance());
            account.setOverdraftLimit(dto.getOverdraftLimit());
            account.setIfscCode(dto.getIfscCode());
            account.setCurrency(dto.getCurrency());
            account.setInterestRate(dto.getInterestRate());

            Account saved = accountRepository.save(account);

            log.info("Account created successfully with accountNumber={}, customerId={}",
                    saved.getAccountNumber(), customer.getId());

            return mapToResponse(saved);

        } catch (Exception ex) {
            log.error("Error creating account for customerId={}", dto.getCustomerId(), ex);
            throw new AccountOperationException("ACCOUNT_CREATE_FAILED", "Failed to create account");
        }
    }

    // UPDATE ACCOUNT
    @Override
    public AccountResponseDTO updateAccount(Long accountId, AccountUpdateRequestDTO dto) {

        log.info("Entering updateAccount() with accountId={}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found for update, id={}", accountId);
                    return new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Account not found");
                });

        try {
            if (dto.getAccountStatus() != null) {
                account.setAccountStatus(dto.getAccountStatus());
            }

            if (dto.getOverdraftLimit() != null) {
                account.setOverdraftLimit(dto.getOverdraftLimit());
            }

            if (dto.getInterestRate() != null) {
                account.setInterestRate(dto.getInterestRate());
            }

            Account updated = accountRepository.save(account);

            log.info("Account updated successfully, accountId={}", accountId);

            return mapToResponse(updated);

        } catch (Exception ex) {
            log.error("Error updating accountId={}", accountId, ex);
            throw new AccountOperationException("ACCOUNT_UPDATE_FAILED", "Failed to update account");
        }
    }

    // GET ACCOUNT BY ID
    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long accountId) {

        log.info("Fetching account by ID={}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found for id={}", accountId);
                    return new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Account not found");
                });

        log.info("Account retrieved successfully, accountId={}", accountId);

        return mapToResponse(account);
    }

    //  GET ACCOUNTS BY CUSTOMER
    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAccountsByCustomerId(Long customerId) {

        log.info("Fetching accounts for customerId={}", customerId);

        List<AccountResponseDTO> accounts = accountRepository
                .findByCustomerIdAndActiveTrue(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        log.info("Total accounts fetched={} for customerId={}", accounts.size(), customerId);

        return accounts;
    }

    // CLOSE ACCOUNT
    @Override
    public void closeAccount(Long accountId) {

        log.warn("Closing account with accountId={}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found for close, id={}", accountId);
                    return new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Account not found");
                });

        try {
            account.setAccountStatus(Account.AccountStatus.CLOSED);
            account.setActive(false);

            accountRepository.save(account);

            log.warn("Account closed successfully, accountId={}", accountId);

        } catch (Exception ex) {
            log.error("Error closing accountId={}", accountId, ex);
            throw new AccountOperationException("ACCOUNT_CLOSE_FAILED", "Failed to close account");
        }
    }

    //  GENERATE ACCOUNT NUMBER
    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    //  MAPPING METHOD
    private AccountResponseDTO mapToResponse(Account account) {

        return AccountResponseDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomer().getId())
                .customerName(account.getCustomer().getFirstName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .overdraftLimit(account.getOverdraftLimit())
                .ifscCode(account.getIfscCode())
                .currency(account.getCurrency())
                .accountOpenedDate(account.getAccountOpenedDate())
                .lastTransactionDate(account.getLastTransactionDate())
                .accountStatus(account.getAccountStatus())
                .interestRate(account.getInterestRate())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

	

}
