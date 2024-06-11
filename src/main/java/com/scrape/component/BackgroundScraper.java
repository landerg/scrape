package com.scrape.component;

import com.scrape.entity.Booking;
import com.scrape.service.DataTransferService;
import com.scrape.service.BookingService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class BackgroundScraper {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DataTransferService dataTransferService;

    private final String baseUrl = "https://www.olx.ua/uk/lvov/q-%D0%BA%D0%B2%D0%B0%D1%80%D1%82%D0%B8%D1%80%D0%B0/";


    @PostConstruct
    public void init() {
        scheduledScrape(); // Start the background scraping task immediately after the bean is constructed
    }

    @Scheduled(fixedRate = 600000) // 600,000 milliseconds = 10 minute
    public void scheduledScrape() {
        System.out.println("Background scraping initiated...");
        try {
            //int savedCount = 0;
            //int deletedCount = 0;
            List<Booking> scrapedData = bookingService.scrapeAndPersist(baseUrl);
            //for (Booking booking : scrapedData) {
                //List<Booking> transferredBookings = dataTransferService.transferDataIfNotExists(booking);
                //savedCount += transferredBookings.size();
                //deletedCount += (transferredBookings.size() > 0) ? 1 : 0;
            //}
            System.out.println("Background scraping completed.");
            //System.out.println("Data saved: " + savedCount + ", Data deleted: " + deletedCount);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Error occurred during background scraping: " + e.getMessage());
        }
    }
}


