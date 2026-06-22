package com.techpalle.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techpalle.exception.ResourceNotFoundException;
import com.techpalle.dto.response.AccountStatementDTO;
import com.techpalle.dto.response.TransactionHistoryDTO;
import com.techpalle.entity.Account;
import com.techpalle.entity.Transaction;
import com.techpalle.repository.AccountRepository;
import com.techpalle.repository.TransactionRepository;
import com.techpalle.service.ReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReportingServiceImpl implements ReportingService {
	
	@Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final TransactionRepository transactionRepository;

    //  GET ACCOUNT STATEMENT
    @Override  
    public AccountStatementDTO getAccountStatement(Long accountId) {

        log.info("Generating account statement → accountId={}", accountId);

        //  Validate account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Statement failed → Account not found: {}", accountId);
                    return new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Account not found");
                });

        //  Fetch transactions 
        List<Transaction> transactions = transactionRepository
                .findByAccountIdOrderByTransactionDateDesc(accountId);

        log.info("Fetched {} transactions for accountId={}", transactions.size(), accountId);

        //  Convert to DTO
        List<TransactionHistoryDTO> history = transactions.stream()
                .map(this::mapToHistoryDTO)
                .collect(Collectors.toList());

        AccountStatementDTO statement = AccountStatementDTO.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerName(account.getCustomer().getFirstName())
                .accountType(account.getAccountType())
                .openingBalance(calculateOpeningBalance(account, transactions))
                .closingBalance(account.getBalance())
                .statementDate(LocalDate.now())
                .transactions(history)
                .build();

        log.info("Statement generated successfully → accountId={}", accountId);

        return statement;
    }

    private TransactionHistoryDTO mapToHistoryDTO(Transaction txn) {

        return TransactionHistoryDTO.builder()
                .id(txn.getId())
                .transactionType(txn.getTransactionType())
                .amount(txn.getAmount())
                .balanceAfterTransaction(txn.getBalanceAfterTransaction())
                .transactionDate(txn.getTransactionDate())
                .referenceNumber(txn.getReferenceNumber())
                .description(txn.getDescription())
                .transactionStatus(txn.getTransactionStatus())
                .build();
    }

    private java.math.BigDecimal calculateOpeningBalance(Account account, List<Transaction> transactions) {

        log.info("Calculating opening balance → accountId={}", account.getId());

        if (transactions == null || transactions.isEmpty()) {
            return account.getBalance();
        }

        Transaction lastTransaction = transactions.get(transactions.size() - 1);

        return lastTransaction.getBalanceAfterTransaction()
                .subtract(lastTransaction.getAmount());

    }


}
