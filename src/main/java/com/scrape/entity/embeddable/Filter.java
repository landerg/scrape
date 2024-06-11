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
public class Filter {
    private String floor;
    private String totalArea;
    private String kitchenArea;
    private String wallType;
    private String roomCount;
    private String bathroomType;
    private String heatingType;
    private String renovationType;
    private String furnitureAvailability;
    private String appliances;
    private String multimedia;
    private String comfortOptions;
    private String communicationOptions;
    private String infrastructure;
}
