package com.scrape.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;


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

}
