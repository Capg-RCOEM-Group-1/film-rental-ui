package com.rcoem.filmrentalui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffDTO {
    private Byte staffId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Boolean active = true;
    private String password;
    // Use strings to hold the URI or ID for the dropdown selections
    private String store;
    private String address;
}
