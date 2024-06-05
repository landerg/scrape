package com.scrape.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {

    private Long id;

    private String title;
    private String location;
    private String propertyDetails;
    private String description;
    private String publicationDate;
    private String mainPhotoUrl;
    private List<String> smallPhotoUrls;
    private String price;
    private String additionalInformation;

    // Getters and setters omitted for brevity
}
