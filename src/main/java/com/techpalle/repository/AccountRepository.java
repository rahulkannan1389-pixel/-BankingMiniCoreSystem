package com.techpalle.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techpalle.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Serializable> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomerIdAndActiveTrue(Long customerId);

    List<Account> findByCustomerIdAndAccountStatusAndActiveTrue(
            Long customerId,
            Account.AccountStatus status
    );

    long countByCustomerId(Long customerId);

    List<Account> findByIfscCodeAndActiveTrue(String ifscCode);


      @Lock(LockModeType.PESSIMISTIC_WRITE)
      @Query("SELECT a FROM Account a WHERE a.id = :id")
      Optional<Account> findByIdForUpdate(@Param("id") Long id);


}
