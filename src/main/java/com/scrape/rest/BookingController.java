package com.scrape.rest;

import org.springframework.ui.Model;
import com.scrape.entity.Booking;
import com.scrape.repository.BookingRepository;
import com.scrape.service.DataTransferService;
import com.scrape.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {

    @Autowired
    private ScraperService scraperService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DataTransferService dataTransferService;

    private final String baseUrl = "https://www.olx.ua/uk/lvov/q-%D0%BA%D0%B2%D0%B0%D1%80%D1%82%D0%B8%D1%80%D0%B0/";

    @GetMapping("/scrape")
    public String scrapeWebsite(@RequestParam(required = false) String url) {
        String targetUrl = (url != null) ? url : baseUrl;
        try {
            List<Booking> scrapedData = scraperService.scrapeAndPersist(targetUrl);
            for (Booking booking : scrapedData) {
                List<Booking> transferredBookings = dataTransferService.transferDataIfNotExists(booking);
            }
            return "Scraping completed.";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error occurred during scraping: " + e.getMessage();
        }
    }

    @GetMapping("/bookings")
    public String getAllBookings(Model model) {
        List<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "bookings";
    }
}


