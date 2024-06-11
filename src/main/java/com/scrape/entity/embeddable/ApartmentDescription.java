package com.scrape.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ApartmentDescription {
    private String typeOfHouse;
    private int floor;
    private int numberOfFloors;
    private int totalArea;
    private int kitchenArea;
    private String wallType;
    private int numberOfRooms;
    private boolean furnished;
}