package com.techpalle.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Entity
@Table(name = "accounts",
        indexes = {
                @Index(name = "idx_account_number", columnList = "account_number"),
                @Index(name = "idx_customer_id", columnList = "customer_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_account_number", columnNames = "account_number")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"customer", "transactions"})@ToString(exclude = {"customer", "transactions"})
@DynamicInsert
@DynamicUpdate

public class Account extends BaseEntity{
	
    @NotBlank(message = "Account number is required")
    @Column(name = "account_number", length = 20, nullable = false, unique = true)
    private String accountNumber;

    /**
     * Many accounts belong to one customer
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_account_customer"))
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @PositiveOrZero
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "overdraft_limit", precision = 19, scale = 2)
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

    @NotBlank(message = "IFSC code is required")
    @Column(name = "ifsc_code", length = 20, nullable = false)
    private String ifscCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private CurrencyType currency = CurrencyType.INR;

    @Column(name = "account_opened_date", nullable = false, updatable = false)
    private LocalDate accountOpenedDate = LocalDate.now();

    @Column(name = "last_transaction_date")
    private LocalDate lastTransactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate = BigDecimal.ZERO;

    /**
     * Optimistic locking for concurrency control
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * One account can have multiple transactions
     */
    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // ================= BUSINESS METHODS =================

    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void deductBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public boolean hasSufficientBalance(BigDecimal amount) {
        return this.balance.add(this.overdraftLimit).compareTo(amount) >= 0;
    }

    // ================= ENUMS =================

    public enum AccountType {
        SAVINGS,
        CURRENT,
        FIXED_DEPOSIT
    }

    public enum AccountStatus {
        ACTIVE,
        FROZEN,
        BLOCKED,
        CLOSED
    }

    public enum CurrencyType {
        INR,
        USD,
        EUR
    }


}
