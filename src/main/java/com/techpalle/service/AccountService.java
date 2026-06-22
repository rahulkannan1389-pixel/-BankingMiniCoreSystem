package com.techpalle.service;

import java.util.List;

import com.techpalle.dto.request.AccountCreateRequestDTO;
import com.techpalle.dto.request.AccountUpdateRequestDTO;
import com.techpalle.dto.response.AccountResponseDTO;

public interface AccountService {

    AccountResponseDTO createAccount(AccountCreateRequestDTO requestDTO);

    AccountResponseDTO updateAccount(Long accountId, AccountUpdateRequestDTO requestDTO);

    AccountResponseDTO getAccountById(Long accountId);

    List<AccountResponseDTO> getAccountsByCustomerId(Long customerId);

    void closeAccount(Long accountId);
}

