package com.techpalle.dto.response;

import com.techpalle.entity.Customer.CustomerStatus;
import com.techpalle.entity.Customer.CustomerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerListDTO {
	private Long id;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;

    private CustomerType customerType;
    private CustomerStatus customerStatus;

}
