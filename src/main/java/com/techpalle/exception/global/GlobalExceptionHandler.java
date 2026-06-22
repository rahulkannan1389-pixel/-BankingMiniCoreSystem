package com.techpalle.exception.global;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.techpalle.dto.response.ApiResponse;
import com.techpalle.exception.BankingException;
import jakarta.servlet.http.HttpServletRequest;

public class GlobalExceptionHandler {

	    @ExceptionHandler(BankingException.class)
	    public ResponseEntity<ApiResponse<?>> handleBankingException(
	            BankingException ex,
	            HttpServletRequest request) {

	        ApiResponse<?> response = ApiResponse.error(
	                400,
	                ex.getMessage(),
	                List.of(ex.getErrorCode())
	        );

	        response.setPath(request.getRequestURI());

	        return ResponseEntity.badRequest().body(response);
	    }

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ApiResponse<?>> handleValidationException(
	            MethodArgumentNotValidException ex,
	            HttpServletRequest request) {

	        List<String> errors = ex.getBindingResult()
	                .getFieldErrors()
	                .stream()
	                .map(error -> error.getField() + ": " + error.getDefaultMessage())
	                .collect(Collectors.toList());

	        ApiResponse<?> response = ApiResponse.error(
	                400,
	                "Validation failed",
	                errors
	        );

	        response.setPath(request.getRequestURI());

	        return ResponseEntity.badRequest().body(response);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiResponse<?>> handleGenericException(
	            Exception ex,
	            HttpServletRequest request) {

	        ApiResponse<?> response = ApiResponse.error(
	                500,
	                "Internal Server Error",
	                List.of(ex.getMessage())
	        );
	        response.setPath(request.getRequestURI());
	        return ResponseEntity.internalServerError().body(response);
	    }
	}

