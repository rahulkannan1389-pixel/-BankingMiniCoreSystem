package com.techpalle;


import com.techpalle.dto.request.CustomerRequestDTO;
import com.techpalle.dto.response.CustomerResponseDTO;
import com.techpalle.entity.Customer;
import com.techpalle.exception.DuplicateResourceException;
import com.techpalle.repository.CustomerRepository;
import com.techpalle.serviceimpl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequestDTO requestDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {

        requestDTO = new CustomerRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setEmail("john@example.com");
        requestDTO.setPhone("9876543210");
        requestDTO.setAadharNumber("123456789012");
        requestDTO.setCustomerType(Customer.CustomerType.INDIVIDUAL);
        requestDTO.setKycVerified(false);

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setKycVerified(false);
    }
    
    @Test
    void testCreateCustomerSuccess() {

        when(customerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        CustomerResponseDTO response = customerService.createCustomer(requestDTO);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomerDuplicateEmail() {

        when(customerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(customer));

        assertThrows(DuplicateResourceException.class,
                () -> customerService.createCustomer(requestDTO));
    }


    @Test
    void testUpdateCustomerSuccess() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        CustomerResponseDTO response =
                customerService.updateCustomer(1L, requestDTO);

        assertNotNull(response);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        CustomerResponseDTO response =
                customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
    }

}
