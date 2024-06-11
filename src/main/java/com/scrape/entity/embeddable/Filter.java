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
    private boolean privatePerson;
    private boolean noCommission;
    private boolean readyToCooperateWithRealtors;
    private boolean internetWorks;
    private boolean elevatorWorks;
    private boolean waterSupplyWorks;
    private boolean heatingWorks;
    private boolean backupPowerConnected;
    private boolean petAllowed;
    private String petType;
}
