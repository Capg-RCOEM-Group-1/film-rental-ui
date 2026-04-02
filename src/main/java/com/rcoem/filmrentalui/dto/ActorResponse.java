package com.rcoem.filmrentalui.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class ActorResponse {
    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    public static class Embedded {
        private List<ActorDTO> actors;
    }
}