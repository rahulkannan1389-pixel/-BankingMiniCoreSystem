package com.techpalle.dto.response;

import java.time.LocalDateTime;

import com.techpalle.entity.Customer.CustomerStatus;
import com.techpalle.entity.Customer.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
	
	    private Long id;

	    private String firstName;
	    private String lastName;

	    private String email;
	    private String phone;

	    private String aadharNumber;

	    private CustomerType customerType;
	    private CustomerStatus customerStatus;

	    private boolean kycVerified;

	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

}
