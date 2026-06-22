package com.techpalle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techpalle.dto.response.AccountStatementDTO;
import com.techpalle.dto.response.ApiResponse;
import com.techpalle.service.ReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportingController {

	 private final ReportingService reportingService;

	    //  GET ACCOUNT STATEMENT
	    @GetMapping("/statement/{accountId}")
	    public ResponseEntity<ApiResponse<AccountStatementDTO>> getAccountStatement(
	            @PathVariable Long accountId) {

	        log.info("REST request → generate account statement: accountId={}", accountId);

	        AccountStatementDTO statement = reportingService.getAccountStatement(accountId);

	        log.info("Account statement generated successfully → accountId={}", accountId);

	        return ResponseEntity.ok(
	                ApiResponse.success(statement, "Account statement generated successfully")
	        );
	    }
}
