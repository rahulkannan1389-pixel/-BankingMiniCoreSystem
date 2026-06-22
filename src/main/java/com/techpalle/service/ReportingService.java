package com.techpalle.service;

import com.techpalle.dto.response.AccountStatementDTO;

public interface ReportingService {
	
	AccountStatementDTO getAccountStatement(Long accountId);

}
