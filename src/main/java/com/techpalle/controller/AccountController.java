package com.techpalle.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techpalle.dto.request.AccountCreateRequestDTO;
import com.techpalle.dto.request.AccountUpdateRequestDTO;
import com.techpalle.dto.response.AccountResponseDTO;
import com.techpalle.dto.response.ApiResponse;
import com.techpalle.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
	
   private final AccountService accountService;

    //  CREATE ACCOUNT
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponseDTO>> createAccount(
            @Valid @RequestBody AccountCreateRequestDTO request) {

        log.info("REST request to create account → customerId={}", request.getCustomerId());

        AccountResponseDTO response = accountService.createAccount(request);

        log.info("Account created successfully → accountNumber={}", response.getAccountNumber());

        return ResponseEntity.ok(ApiResponse.success(response, "Account created successfully"));
    }

    //  UPDATE ACCOUNT
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> updateAccount(
            @PathVariable Long accountId,
            @RequestBody AccountUpdateRequestDTO request) {

        log.info("REST request to update account → accountId={}", accountId);

        AccountResponseDTO response = accountService.updateAccount(accountId, request);

        log.info("Account updated successfully → accountId={}", accountId);

        return ResponseEntity.ok(ApiResponse.success(response, "Account updated successfully"));
    }

    //  GET ACCOUNT BY ID
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> getAccountById(
            @PathVariable Long accountId) {

        log.info("REST request to fetch account → accountId={}", accountId);

        AccountResponseDTO response = accountService.getAccountById(accountId);

        return ResponseEntity.ok(ApiResponse.success(response, "Account fetched successfully"));
    }

    //  GET ACCOUNTS BY CUSTOMER
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AccountResponseDTO>>> getAccountsByCustomer(
            @PathVariable Long customerId) {

        log.info("REST request → fetch accounts for customerId={}", customerId);

        List<AccountResponseDTO> accounts = accountService.getAccountsByCustomerId(customerId);

        log.info("Total accounts fetched={} for customerId={}", accounts.size(), customerId);

        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts fetched successfully"));
    }

    // CLOSE ACCOUNT
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> closeAccount(
            @PathVariable Long accountId) {

        log.warn("REST request to close account → accountId={}", accountId);

        accountService.closeAccount(accountId);

        log.warn("Account closed successfully → accountId={}", accountId);

        return ResponseEntity.ok(ApiResponse.success(null, "Account closed successfully"));
    }


}
