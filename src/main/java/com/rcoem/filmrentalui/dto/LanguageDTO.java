package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {
    private String id;
    private String name;
}
