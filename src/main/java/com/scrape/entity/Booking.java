package com.scrape.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String price;
    private String location;
    private String date;
    private String area;
    private String mainLink;

    public Booking(Long id, String title, String price, String location, String date, String area, String mainLink) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.location = location;
        this.date = date;
        this.area = area;
        this.mainLink = mainLink;
    }

    // Getters and Setters
}
