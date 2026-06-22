package com.techpalle.service;

import java.util.List;

import com.techpalle.dto.response.CustomerResponseDTO;
import com.techpalle.dto.request.CustomerRequestDTO;
import com.techpalle.dto.response.CustomerListDTO;


public interface CustomerService {
	

	    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);

	    CustomerResponseDTO updateCustomer(Long customerId, CustomerRequestDTO requestDTO);

	    CustomerResponseDTO getCustomerById(Long customerId);

	    List<CustomerListDTO> getAllCustomers();

	    CustomerResponseDTO getCustomerByEmail(String email);

	    CustomerResponseDTO verifyKYC(Long customerId);

	    void deleteCustomer(Long customerId);

	

}
