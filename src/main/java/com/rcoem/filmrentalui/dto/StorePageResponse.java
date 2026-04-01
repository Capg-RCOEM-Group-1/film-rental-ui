package com.rcoem.filmrentalui.dto;

import lombok.Data;
import java.util.List;

@Data
public class StorePageResponse {
    private Embedded _embedded;
    private PageData page;

    @Data
    public static class Embedded {
        private List<StoreDTO> stores;
    }
}
