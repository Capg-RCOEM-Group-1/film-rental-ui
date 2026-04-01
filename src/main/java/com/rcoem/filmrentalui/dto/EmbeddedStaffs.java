package com.rcoem.filmrentalui.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmbeddedStaffs {
    private List<StaffDTO> staffs; // Must match the JSON key "staffs"
}
