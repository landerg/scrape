package com.scrape.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ApartmentDescription {
    @Column(columnDefinition = "TEXT")
    private String description;
}