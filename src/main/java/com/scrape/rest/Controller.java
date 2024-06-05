package com.scrape.rest;

import com.scrape.entity.Booking;
import com.scrape.scrape.Scraper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class Controller {

    private final Scraper scraper;

    public Controller(Scraper scraper) {
        this.scraper = scraper;
    }

    @GetMapping("/bookings")
    public List<Booking> getBookings() throws IOException {
        String url = "https://dom.ria.com/uk/arenda-kvartir/"; // Replace with the actual URL
        return scraper.scrape(url);
    }

}
