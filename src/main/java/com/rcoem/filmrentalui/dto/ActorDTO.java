package com.rcoem.filmrentalui.dto;


import lombok.Data;

@Data
public class ActorDTO {

 private String firstName;
    private String lastName;
    
    // We create a helper method here so we don't have to change much in HTML
    public String getFullName() {
        if (firstName == null) return "Unknown";
        return firstName + " " + (lastName != null ? lastName : "");
    }
 
}
