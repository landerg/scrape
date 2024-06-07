package com.scrape.rest;

import com.scrape.entity.Booking;
import com.scrape.service.DataTransferService;
import com.scrape.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return scraperService.getAllBookings();
    }
}

/*@RestController
public class Controller {

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/")
    public List<Booking> scrape() throws IOException, ExecutionException, InterruptedException {
        String url = "https://www.olx.ua/uk/lvov/q-%D0%BA%D0%B2%D0%B0%D1%80%D1%82%D0%B8%D1%80%D0%B0/"; // Replace with the URL you want to scrape
        scraperService.scrapeAndPersist(url);
        return scraperService.scrapeAllPages(url);
    }
}*/

