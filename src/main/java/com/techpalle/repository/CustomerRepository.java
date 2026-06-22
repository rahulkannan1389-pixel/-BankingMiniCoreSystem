package com.techpalle.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techpalle.entity.Customer;

public interface CustomerRepository extends JpaRepository <Customer, Serializable>{
	

	    Optional<Customer> findByEmail(String email);

	    Optional<Customer> findByPhone(String phone);

	    Optional<Customer> findByAadharNumber(String aadharNumber);

	    List<Customer> findByActiveTrue();

	    List<Customer> findByKycVerifiedTrueAndActiveTrue();

	    long countByActiveTrue();

}
