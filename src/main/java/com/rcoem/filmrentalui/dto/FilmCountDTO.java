package com.rcoem.filmrentalui.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilmCountDTO {
    private String title;
    private Long count;
    private String rating;
    private Integer length;
}
