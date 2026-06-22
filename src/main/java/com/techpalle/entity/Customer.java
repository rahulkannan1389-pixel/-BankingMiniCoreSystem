package com.techpalle.entity;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.*;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "customers",
        indexes = {
                @Index(name = "idx_customer_email", columnList = "email"),
                @Index(name = "idx_customer_phone", columnList = "phone")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "uk_customer_email"),
                @UniqueConstraint(columnNames = "phone", name = "uk_customer_phone"),
                @UniqueConstraint(columnNames = "aadhar_number", name = "uk_customer_aadhar")
        })


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "accounts")@ToString(exclude = "accounts")
@DynamicInsert
@DynamicUpdate
public class Customer extends BaseEntity {

	    @NotBlank
	    @Column(name = "first_name", length = 50, nullable = false)
	    private String firstName;

	    @Column(name = "last_name", length = 50)
	    private String lastName;

	    @Email
	    @NotBlank
	    @Column(name = "email", length = 100, nullable = false)
	    private String email;

	    @NotBlank
	    @Pattern(regexp = "^[0-9]{10}$")
	    @Column(name = "phone", length = 20, nullable = false)
	    private String phone;

	    @NotBlank
	    @Pattern(regexp = "^[0-9]{12}$")
	    @Column(name = "aadhar_number", nullable = false)
	    private String aadharNumber;    

	    @Enumerated(EnumType.STRING)
	    @Column(name = "customer_type", nullable = false)
	    private CustomerType customerType;

	    @Column(name = "kyc_verified", nullable = false)
	    private boolean kycVerified = false;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "customer_status", nullable = false)
	    private CustomerStatus customerStatus = CustomerStatus.ACTIVE;

	    @OneToMany(mappedBy = "customer",
	            cascade = CascadeType.ALL,
	            orphanRemoval = true,
	            fetch = FetchType.LAZY)
	    private List<Account> accounts = new ArrayList<>();

	    // Relationship helpers
	    public void addAccount(Account account) {
	        accounts.add(account);
	        account.setCustomer(this);
	    }

	    public void removeAccount(Account account) {
	        accounts.remove(account);
	        account.setCustomer(null);
	    }

	    public enum CustomerType {
	        INDIVIDUAL, CORPORATE, NRI
	    }

	    public enum CustomerStatus {
	        ACTIVE, INACTIVE, BLOCKED, CLOSED
	    }
}
