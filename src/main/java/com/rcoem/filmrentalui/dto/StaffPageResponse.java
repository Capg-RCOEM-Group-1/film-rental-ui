package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffPageResponse {
    @JsonProperty("_embedded")
    private EmbeddedStaffs embedded;
    private PageData page;
}
