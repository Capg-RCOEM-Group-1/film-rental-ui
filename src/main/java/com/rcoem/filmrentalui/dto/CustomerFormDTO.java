package com.rcoem.filmrentalui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerFormDTO {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private Byte storeId;     // Matches Store ID type
    private Short addressId;  // Matches Address ID type
}
