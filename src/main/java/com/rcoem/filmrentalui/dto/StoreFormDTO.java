package com.rcoem.filmrentalui.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreFormDTO {
    private Byte storeId;
    private Short addressId;
    private Byte managerStaffId;
}