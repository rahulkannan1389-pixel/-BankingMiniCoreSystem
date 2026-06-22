package com.techpalle.service;

import java.util.List;
import com.techpalle.dto.request.DepositRequestDTO;
import com.techpalle.dto.request.FundTransferRequestDTO;
import com.techpalle.dto.request.WithdrawalRequestDTO;
import com.techpalle.dto.response.TransactionHistoryDTO;
import com.techpalle.dto.response.TransactionResponseDTO;
import com.techpalle.dto.response.TransferResponseDTO;

public interface TransactionService {

    TransactionResponseDTO deposit(DepositRequestDTO requestDTO);

    TransactionResponseDTO withdraw(WithdrawalRequestDTO requestDTO);

    TransferResponseDTO transferFunds(FundTransferRequestDTO requestDTO);

    List<TransactionHistoryDTO> getTransactionHistory(Long accountId);
}

