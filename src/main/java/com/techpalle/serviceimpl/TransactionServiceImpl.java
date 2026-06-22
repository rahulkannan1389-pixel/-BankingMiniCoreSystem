package com.techpalle.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techpalle.dto.request.DepositRequestDTO;
import com.techpalle.dto.request.FundTransferRequestDTO;
import com.techpalle.dto.request.WithdrawalRequestDTO;
import com.techpalle.dto.response.TransactionHistoryDTO;
import com.techpalle.dto.response.TransactionResponseDTO;
import com.techpalle.dto.response.TransferResponseDTO;
import com.techpalle.entity.Account;
import com.techpalle.entity.Transaction;
import com.techpalle.exception.InsufficientBalanceException;
import com.techpalle.exception.InvalidTransactionException;
import com.techpalle.exception.ResourceNotFoundException;
import com.techpalle.repository.AccountRepository;
import com.techpalle.repository.TransactionRepository;
import com.techpalle.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired 
   private  AccountRepository accountRepository;

    @Autowired 
    private  TransactionRepository transactionRepository;

    //  DEPOSIT
    @Override
    public TransactionResponseDTO deposit(DepositRequestDTO dto) {

        log.info("Deposit initiated → accountId={}, amount={}", dto.getAccountId(), dto.getAmount());
        
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
               throw new InvalidTransactionException("INVALID_AMOUNT", "Amount must be greater than zero");
           }

        Account account = accountRepository.findByIdForUpdate(dto.getAccountId())
                .orElseThrow(() -> {
                    log.error("Deposit failed → Account not found: {}", dto.getAccountId());
                    return new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Account not found");
                });

        if (!account.isActive()) {
            throw new InvalidTransactionException("ACCOUNT_INACTIVE", "Account is not active");
        }

        account.addBalance(dto.getAmount());

        Transaction txn = createTransaction(
                account,
                null,
                dto.getAmount(),
                Transaction.TransactionType.DEPOSIT,
                dto.getDescription()
        );

        log.info("Deposit successful → accountId={}, balance={}", account.getId(), account.getBalance());

        return mapToResponse(txn);
    }

    //  WITHDRAW
    @Override
    public TransactionResponseDTO withdraw(WithdrawalRequestDTO dto) {

        log.info("Withdraw initiated → accountId={}, amount={}", dto.getAccountId(), dto.getAmount());
        
    if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidTransactionException("INVALID_AMOUNT", "Amount must be greater than zero");
    }

        Account account = accountRepository.findByIdForUpdate(dto.getAccountId())
                .orElseThrow(() -> {
                    log.error("Withdraw failed → Account not found: {}", dto.getAccountId());
                    return new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Account not found");
                });

        if (!account.isActive()) {
            throw new InvalidTransactionException("ACCOUNT_INACTIVE", "Account is not active");
        }

        if (!account.hasSufficientBalance(dto.getAmount())) {
            log.warn("Withdraw failed → insufficient balance, accountId={}", account.getId());
            throw new InsufficientBalanceException("INSUFFICIENT_BALANCE", "Insufficient balance");
        }

        account.deductBalance(dto.getAmount());

        Transaction txn = createTransaction(
                account,
                null,
                dto.getAmount(),
                Transaction.TransactionType.WITHDRAWAL,
                dto.getDescription()
        );

        log.info("Withdrawal successful → accountId={}, balance={}", account.getId(), account.getBalance());

        return mapToResponse(txn);
    }

    //  TRANSFER (CRITICAL)
    @Override
    public TransferResponseDTO transferFunds(FundTransferRequestDTO dto) {

        log.info("Transfer initiated → from={}, to={}, amount={}",
                dto.getFromAccountId(), dto.getToAccountId(), dto.getAmount());
        
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
               throw new InvalidTransactionException("INVALID_AMOUNT", "Amount must be greater than zero");
           }

        if (dto.getFromAccountId().equals(dto.getToAccountId())) {
            throw new InvalidTransactionException("SAME_ACCOUNT", "Cannot transfer to same account");
        }

        //  DEADLOCK PREVENTION
        Account fromAccount;
        Account toAccount;

        if (dto.getFromAccountId() < dto.getToAccountId()) {
            fromAccount = accountRepository.findByIdForUpdate(dto.getFromAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Sender not found"));

            toAccount = accountRepository.findByIdForUpdate(dto.getToAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Receiver not found"));
        } else {
            toAccount = accountRepository.findByIdForUpdate(dto.getToAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Receiver not found"));

            fromAccount = accountRepository.findByIdForUpdate(dto.getFromAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("ACCOUNT_NOT_FOUND", "Sender not found"));
        }

        if (!fromAccount.hasSufficientBalance(dto.getAmount())) {
            log.warn("Transfer failed → insufficient balance, account={}", fromAccount.getId());
            throw new InsufficientBalanceException("INSUFFICIENT_BALANCE", "Not enough balance");
        }

        fromAccount.deductBalance(dto.getAmount());
        toAccount.addBalance(dto.getAmount());

        String ref = generateReference();

        Transaction debit = createTransaction(
                fromAccount, toAccount, dto.getAmount(),
                Transaction.TransactionType.TRANSFER_OUT,
                dto.getDescription(), ref
        );

        createTransaction(
                toAccount, fromAccount, dto.getAmount(),
                Transaction.TransactionType.TRANSFER_IN,
                dto.getDescription(), ref
        );

        log.info("Transfer successful → reference={}, amount={}", ref, dto.getAmount());

        return TransferResponseDTO.builder()
                .transactionId(debit.getId())
                .referenceNumber(ref)
                .fromAccountId(fromAccount.getId())
                .toAccountId(toAccount.getId())
                .fromAccountNumber(fromAccount.getAccountNumber())
                .toAccountNumber(toAccount.getAccountNumber())
                .amount(dto.getAmount())
                .transactionDate(LocalDateTime.now())
                .status(Transaction.TransactionStatus.SUCCESS)
                .description(dto.getDescription())
                .build();
    }

    //  HISTORY
    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryDTO> getTransactionHistory(Long accountId) {

        log.info("Fetching transaction history → accountId={}", accountId);

        return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId)
                .stream()
                .map(this::mapToHistory)
                .collect(Collectors.toList());
    }

    //  HELPER METHODS
    private Transaction createTransaction(Account acc, Account toAcc,
                                          java.math.BigDecimal amount,
                                          Transaction.TransactionType type,
                                          String desc) {

        return createTransaction(acc, toAcc, amount, type, desc, generateReference());
    }

    private Transaction createTransaction(Account acc, Account toAcc,
                                          java.math.BigDecimal amount,
                                          Transaction.TransactionType type,
                                          String desc,
                                          String ref) {

        Transaction txn = new Transaction();
        txn.setAccount(acc);
        txn.setTransferToAccount(toAcc);
        txn.setAmount(amount);
        txn.setTransactionType(type);
        txn.setBalanceAfterTransaction(acc.getBalance());
        txn.setTransactionDate(LocalDateTime.now());
        txn.setReferenceNumber(ref);
        txn.setDescription(desc);
        txn.setTransactionStatus(Transaction.TransactionStatus.SUCCESS);

        return transactionRepository.save(txn);
    }

    private String generateReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private TransactionResponseDTO mapToResponse(Transaction txn) {
        return TransactionResponseDTO.builder()
                .id(txn.getId())
                .accountId(txn.getAccount().getId())
                .accountNumber(txn.getAccount().getAccountNumber())
                .transactionType(txn.getTransactionType())
                .amount(txn.getAmount())
                .balanceAfterTransaction(txn.getBalanceAfterTransaction())
                .transactionDate(txn.getTransactionDate())
                .referenceNumber(txn.getReferenceNumber())
                .description(txn.getDescription())
                .transactionStatus(txn.getTransactionStatus())
                .build();
    }

    private TransactionHistoryDTO mapToHistory(Transaction txn) {
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

}
