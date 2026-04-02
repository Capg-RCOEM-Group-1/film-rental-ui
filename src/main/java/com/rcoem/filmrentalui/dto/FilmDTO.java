package com.rcoem.filmrentalui.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class FilmDTO {
//     private String filmId;
//     private String title;
//     private String description;
//     private Integer releaseYear;
//     private Integer rentalDuration;
//     private BigDecimal rentalRate;
//      private String language;
//     private Integer length;
//     private BigDecimal replacementCost;
//     private String rating; 
//     private List<String> specialFeatures; 
//     private String lastUpdate;

//     // private List<ActorDTO> actors;

//     @JsonProperty("_links")
// private Links links;

// public Long getIdFromLink() {
//     if (links != null && links.self != null) {
//         String href = links.self.href;
//         return Long.parseLong(href.substring(href.lastIndexOf("/") + 1));
//     }
//     return null;
// }

// public static class Links {
//     public Self self;
// }

// public static class Self {
//     public String href;
// }

// }
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmDTO {
    private String filmId;
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer rentalDuration;
    private BigDecimal rentalRate;
    private String language; // Matches @Value("#{target.language.name}") String
    private Integer length;
    private BigDecimal replacementCost;
    private String rating;

  private List<String> specialFeatures; 

    private String lastUpdate;

    @JsonProperty("_links")
    private Links links;

    public Long getIdFromLink() {
        if (links != null && links.self != null) {
            String href = links.self.href;
            return Long.parseLong(href.substring(href.lastIndexOf("/") + 1));
        }
        // Fallback: If projection provides filmId directly
        if (filmId != null)
            return Long.parseLong(filmId);
        return null;
    }

    public static class Links {
        public Self self;
    }

    public static class Self {
        public String href;
    }
}
