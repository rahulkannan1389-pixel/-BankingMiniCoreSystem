package com.techpalle.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "transactions",
        indexes = {
                @Index(name = "idx_account_id", columnList = "account_id"),
                @Index(name = "idx_transaction_date", columnList = "transaction_date"),
                @Index(name = "idx_reference_number", columnList = "reference_number")
        })


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"account", "transferToAccount"})
@ToString(exclude = {"account", "transferToAccount"})
@DynamicInsert
@DynamicUpdate

public class Transaction extends BaseEntity{
	

	 @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "account_id",
	            nullable = false,
	            foreignKey = @ForeignKey(name = "fk_transaction_account"))
	    private Account account;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "transaction_type", nullable = false)
	    private TransactionType transactionType;

	    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
	    private BigDecimal amount;

	    @Column(name = "balance_after_transaction", nullable = false, precision = 19, scale = 2)
	    private BigDecimal balanceAfterTransaction;

	    @CreationTimestamp
	    @Column(name = "transaction_date", nullable = false, updatable = false)
	    private LocalDateTime transactionDate;

	    @NotBlank
	    @Column(name = "reference_number", length = 50, nullable = false, unique = true, updatable = false)
	    private String referenceNumber;

	    @Column(name = "description", length = 500)
	    private String description;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "transaction_status", nullable = false)
	    private TransactionStatus transactionStatus = TransactionStatus.SUCCESS;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "transfer_to_account_id",
	            foreignKey = @ForeignKey(name = "fk_transaction_transfer_account"))
	    private Account transferToAccount;

	    @Column(name = "remarks", length = 500)
	    private String remarks;

	    @Column(name = "initiated_from_ip", length = 45)
	    private String initiatedFromIp;

	    @Column(name = "device_info", length = 255)
	    private String deviceInfo;

	    // ================= ENUMS =================

	    public enum TransactionType {
	        DEPOSIT,
	        WITHDRAWAL,
	        TRANSFER_OUT,
	        TRANSFER_IN
	    }

	    public enum TransactionStatus {
	        SUCCESS,
	        FAILED,
	        PENDING,
	        REVERSED
	    }


}
