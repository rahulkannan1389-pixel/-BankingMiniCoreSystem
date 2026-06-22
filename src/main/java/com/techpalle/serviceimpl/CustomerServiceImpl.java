package com.techpalle.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techpalle.dto.request.CustomerRequestDTO;
import com.techpalle.dto.response.CustomerListDTO;
import com.techpalle.dto.response.CustomerResponseDTO;
import com.techpalle.entity.Customer;
import com.techpalle.exception.DuplicateResourceException;
import com.techpalle.exception.ResourceNotFoundException;
import com.techpalle.repository.CustomerRepository;
import com.techpalle.service.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {
     
	@Autowired
	 private CustomerRepository customerRepository;

	    //  CREATE CUSTOMER
	    @Override
	    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {

	        log.info("Entering createCustomer() with email={}, phone={}", dto.getEmail(), dto.getPhone());

	        if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
	            log.warn("Duplicate email detected: {}", dto.getEmail());
	            throw new DuplicateResourceException("EMAIL_EXISTS", "Email already exists");
	        }

	        try {
	            Customer customer = new Customer();
	            customer.setFirstName(dto.getFirstName());
	            customer.setLastName(dto.getLastName());
	            customer.setEmail(dto.getEmail());
	            customer.setPhone(dto.getPhone());
	            customer.setAadharNumber(dto.getAadharNumber());
	            customer.setCustomerType(dto.getCustomerType());
	            customer.setKycVerified(dto.isKycVerified());

	            Customer saved = customerRepository.save(customer);

	            log.info("Customer created successfully with ID={} and email={}", saved.getId(), saved.getEmail());

	            return mapToResponse(saved);

	        } catch (Exception ex) {
	            log.error("Error occurred while creating customer with email={}", dto.getEmail(), ex);
	            throw new RuntimeException("Failed to create customer");
	        }
	    }

	    //UPDATE CUSTOMER
	    @Override
	    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto) {

	        log.info("Entering updateCustomer() with id={}", id);

	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> {
	                    log.error("Customer not found for update, id={}", id);
	                    return new ResourceNotFoundException("NOT_FOUND", "Customer not found");
	                });

	        try {
	            customer.setFirstName(dto.getFirstName());
	            customer.setLastName(dto.getLastName());
                customer.setEmail(dto.getEmail());
                customer.setPhone(dto.getPhone());
               customer.setKycVerified(dto.isKycVerified());


	            Customer updated = customerRepository.save(customer);

	            log.info("Customer updated successfully, id={}", updated.getId());

	            return mapToResponse(updated);

	        } catch (Exception ex) {
	            log.error("Error updating customer id={}", id, ex);
	            throw new RuntimeException("Failed to update customer");
	        }
	    }

	    //  GET BY ID
	    @Override
	    @Transactional(readOnly = true)
	    public CustomerResponseDTO getCustomerById(Long id) {

	        log.info("Fetching customer by ID={}", id);

	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> {
	                    log.error("Customer not found for ID={}", id);
	                    return new ResourceNotFoundException("NOT_FOUND", "Customer not found");
	                });

	        log.info("Customer retrieved successfully, id={}", id);

	        return mapToResponse(customer);
	    }

	    //  GET ALL
	    @Override
	    @Transactional(readOnly = true)
	    public List<CustomerListDTO> getAllCustomers() {

	        log.info("Fetching all active customers");

	        List<CustomerListDTO> customers = customerRepository.findByActiveTrue()
	                .stream()
	                .map(this::mapToList)
	                .collect(Collectors.toList());

	        log.info("Total customers fetched={}", customers.size());

	        return customers;
	    }

	    //  GET BY EMAIL
	    @Override
	    @Transactional(readOnly = true)
	    public CustomerResponseDTO getCustomerByEmail(String email) {

	        log.info("Fetching customer by email={}", email);

	        Customer customer = customerRepository.findByEmail(email)
	                .orElseThrow(() -> {
	                    log.error("Customer not found with email={}", email);
	                    return new ResourceNotFoundException("NOT_FOUND", "Customer not found");
	                });

	        log.info("Customer retrieved successfully with email={}", email);

	        return mapToResponse(customer);
	    }

	    // DELETE CUSTOMER
	    @Override
	    public void deleteCustomer(Long id) {

	        log.info("Deleting customer with ID={}", id);

	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> {
	                    log.error("Customer not found for delete, id={}", id);
	                    return new ResourceNotFoundException("NOT_FOUND", "Customer not found");
	                });

	        customer.setActive(false);
	        customerRepository.save(customer);

	        log.warn("Customer soft-deleted successfully, id={}", id);
	    }

	    //  VERIFY KYC
	    @Override
	    public CustomerResponseDTO verifyKYC(Long id) {

	        log.info("Verifying KYC for customer ID={}", id);

	        Customer customer = customerRepository.findById(id)
	                .orElseThrow(() -> {
	                    log.error("Customer not found for KYC verification, id={}", id);
	                    return new ResourceNotFoundException("NOT_FOUND", "Customer not found");
	                });

	        customer.setKycVerified(true);

	        Customer updated = customerRepository.save(customer);

	        log.info("KYC verified successfully for customer ID={}", id);

	        return mapToResponse(updated);
	    }

	    //  MAPPING METHODS
	    private CustomerResponseDTO mapToResponse(Customer c) {

	        return CustomerResponseDTO.builder()
	                .id(c.getId())
	                .firstName(c.getFirstName())
	                .lastName(c.getLastName())
	                .email(c.getEmail())
	                .phone(c.getPhone())
	                .aadharNumber(c.getAadharNumber())
	                .customerType(c.getCustomerType())
	                .customerStatus(c.getCustomerStatus())
	                .kycVerified(c.isKycVerified())
	                .createdAt(c.getCreatedAt())
	                .updatedAt(c.getUpdatedAt())
	                .build();
	    }

	    private CustomerListDTO mapToList(Customer c) {

	        return CustomerListDTO.builder()
	                .id(c.getId())
	                .firstName(c.getFirstName())
	                .lastName(c.getLastName())
	                .email(c.getEmail())
	                .phone(c.getPhone())
	                .customerType(c.getCustomerType())
	                .customerStatus(c.getCustomerStatus())
	                .build();
	    }


}
