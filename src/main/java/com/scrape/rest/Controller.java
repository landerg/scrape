package com.scrape.rest;

import com.scrape.entity.Booking;
import com.scrape.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/")
    public List<Booking> scrape() throws IOException {
        String url = "https://www.olx.ua/uk/lvov/q-%D0%BA%D0%B2%D0%B0%D1%80%D1%82%D0%B8%D1%80%D0%B0/"; // Replace with the URL you want to scrape
        return scraperService.scrapeAllPages(url);
    }
}

