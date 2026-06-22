package com.techpalle.dto.request;

import com.techpalle.entity.Customer.CustomerType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequestDTO {
	
	   @NotBlank(message = "First name is required")
	    private String firstName;

	    private String lastName;

	    @Email(message = "Invalid email format")
	    @NotBlank(message = "Email is required")
	    private String email;

	    @NotBlank(message = "Phone number is required")
	    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
	    private String phone;

	    @NotBlank(message = "Aadhar number is required")
	    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhar must be 12 digits")
	    private String aadharNumber;

	    @NotNull(message = "Customer type is required")
	    private CustomerType customerType;

	    private boolean kycVerified;

}
