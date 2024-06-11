package com.scrape.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Appartament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String price;
    private String location;
    private String area;
    private String description;
    private String mainLink;

    @Embedded
    private AppartamentDescription appartamentDescription;

    @Embedded
    private Filter filter;

    // Add dependencies with foreign keys to other classes
    @OneToOne(mappedBy = "appartament", cascade = CascadeType.ALL)
    private Booking booking;

    @OneToOne(mappedBy = "appartament", cascade = CascadeType.ALL)
    private DeletedBooking deletedBooking;
}

