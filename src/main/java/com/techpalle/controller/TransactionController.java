package com.techpalle.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techpalle.dto.request.DepositRequestDTO;
import com.techpalle.dto.request.FundTransferRequestDTO;
import com.techpalle.dto.request.WithdrawalRequestDTO;
import com.techpalle.dto.response.ApiResponse;
import com.techpalle.dto.response.TransactionHistoryDTO;
import com.techpalle.dto.response.TransactionResponseDTO;
import com.techpalle.dto.response.TransferResponseDTO;
import com.techpalle.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

   private final TransactionService transactionService;

    //  DEPOSIT
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> deposit(
            @Valid @RequestBody DepositRequestDTO request) {

        log.info("REST request → deposit: accountId={}, amount={}",
                request.getAccountId(), request.getAmount());

        TransactionResponseDTO response = transactionService.deposit(request);

        log.info("Deposit successful → transactionId={}", response.getId());

        return ResponseEntity.ok(ApiResponse.success(response, "Deposit successful"));
    }

    //  WITHDRAW
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> withdraw(
            @Valid @RequestBody WithdrawalRequestDTO request) {

        log.info("REST request → withdraw: accountId={}, amount={}",
                request.getAccountId(), request.getAmount());

        TransactionResponseDTO response = transactionService.withdraw(request);

        log.info("Withdrawal successful → transactionId={}", response.getId());

        return ResponseEntity.ok(ApiResponse.success(response, "Withdrawal successful"));
    }

    // ✅TRANSFER (MOST IMPORTANT)
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransferResponseDTO>> transfer(
            @Valid @RequestBody FundTransferRequestDTO request) {

        log.info("REST request → transfer: fromAccount={}, toAccount={}, amount={}",
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());

        TransferResponseDTO response = transactionService.transferFunds(request);

        log.info("Transfer successful → referenceNumber={}", response.getReferenceNumber());

        return ResponseEntity.ok(ApiResponse.success(response, "Transfer successful"));
    }

    //  TRANSACTION HISTORY
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionHistoryDTO>>> getTransactionHistory(
            @PathVariable Long accountId) {

        log.info("REST request → transaction history: accountId={}", accountId);

        List<TransactionHistoryDTO> history =
                transactionService.getTransactionHistory(accountId);

        log.info("Transaction history fetched → count={}", history.size());

        return ResponseEntity.ok(ApiResponse.success(
                history,
                "Transaction history fetched successfully"
        ));
    }


}
