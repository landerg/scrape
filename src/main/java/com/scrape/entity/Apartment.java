package com.scrape.entity;

import com.scrape.entity.embeddable.ApartmentDescription;
import com.scrape.entity.embeddable.Filter;
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
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String price;
    private String location;
    private String date;
    private String mainLink;

    // Remove direct description mapping if it exists
    // @Column(name = "description")
    // private String description;

    // Replace direct description mapping with embedded ApartmentDescription
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "description", column = @Column(name = "apartment_description"))
    })
    private ApartmentDescription apartmentDescription;

    @Embedded
    private Filter filter;

    // Add dependencies with foreign keys to other classes
    @OneToOne(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Booking booking;

    @OneToOne(mappedBy = "apartment", cascade = CascadeType.ALL)
    private DeletedBooking deletedBooking;
}
