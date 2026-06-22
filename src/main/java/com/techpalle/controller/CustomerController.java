package com.techpalle.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techpalle.dto.request.CustomerRequestDTO;
import com.techpalle.dto.response.ApiResponse;
import com.techpalle.dto.response.CustomerListDTO;
import com.techpalle.dto.response.CustomerResponseDTO;
import com.techpalle.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j

public class CustomerController  {
	
     @Autowired
	 private final CustomerService customerService;

	    //  CREATE CUSTOMER
	    @PostMapping
	    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(
	            @Valid @RequestBody CustomerRequestDTO request) {

	        log.info("REST request to create customer → email={}", request.getEmail());

	        CustomerResponseDTO response = customerService.createCustomer(request);

	        log.info("Customer created successfully → id={}", response.getId());

	        return ResponseEntity.ok(ApiResponse.success(response, "Customer created successfully"));
	    }

	    //  UPDATE CUSTOMER
	    @PutMapping("/{customerId}")
	    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(
	            @PathVariable Long customerId,
	            @Valid @RequestBody CustomerRequestDTO request) {

	        log.info("REST request to update customer → id={}", customerId);

	        CustomerResponseDTO response = customerService.updateCustomer(customerId, request);

	        log.info("Customer updated successfully → id={}", customerId);

	        return ResponseEntity.ok(ApiResponse.success(response, "Customer updated successfully"));
	    }

	    //  GET CUSTOMER BY ID
	    @GetMapping("/{customerId}")
	    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(
	            @PathVariable Long customerId) {

	        log.info("REST request to fetch customer → id={}", customerId);

	        CustomerResponseDTO response = customerService.getCustomerById(customerId);

	        return ResponseEntity.ok(ApiResponse.success(response, "Customer fetched successfully"));
	    }

	    // GET ALL CUSTOMERS
	    @GetMapping
	    public ResponseEntity<ApiResponse<List<CustomerListDTO>>> getAllCustomers() {

	        log.info("REST request to fetch all customers");

	        List<CustomerListDTO> customers = customerService.getAllCustomers();

	        log.info("Total customers fetched={}", customers.size());

	        return ResponseEntity.ok(ApiResponse.success(customers, "Customers fetched successfully"));
	    }

	    // GET CUSTOMER BY EMAIL
	    @GetMapping("/email")
	    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerByEmail(
	            @RequestParam String email) {

	        log.info("REST request to fetch customer by email={}", email);

	        CustomerResponseDTO response = customerService.getCustomerByEmail(email);

	        return ResponseEntity.ok(ApiResponse.success(response, "Customer fetched successfully"));
	    }

	    //  DELETE CUSTOMER 
	    @DeleteMapping("/{customerId}")
	    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
	            @PathVariable Long customerId) {

	        log.warn("REST request to delete customer → id={}", customerId);

	        customerService.deleteCustomer(customerId);

	        log.warn("Customer deleted successfully → id={}", customerId);

	        return ResponseEntity.ok(ApiResponse.success(null, "Customer deleted successfully"));
	    }

	    //  VERIFY KYC
	    @PutMapping("/{customerId}/kyc")
	    public ResponseEntity<ApiResponse<CustomerResponseDTO>> verifyKYC(
	            @PathVariable Long customerId) {

	        log.info("REST request to verify KYC → customerId={}", customerId);

	        CustomerResponseDTO response = customerService.verifyKYC(customerId);

	        log.info("KYC verified successfully → customerId={}", customerId);

	        return ResponseEntity.ok(ApiResponse.success(response, "KYC verified successfully"));
	    }


}
