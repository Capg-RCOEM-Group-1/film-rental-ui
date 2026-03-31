package com.rcoem.filmrentalui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageData {
    private int size;
    private int totalElements;
    private int totalPages;
    private int number;
}
