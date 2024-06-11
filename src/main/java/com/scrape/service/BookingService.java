package com.scrape.service;

import com.scrape.entity.Apartment;
import com.scrape.entity.Booking;
import com.scrape.repository.BookingRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public void deleteDuplicates() {
        List<Booking> bookings = bookingRepository.findAll();
        Set<String> seen = new HashSet<>();
        for (Booking booking : bookings) {
            String uniqueKey = booking.getTitle() + booking.getPrice();
            if (seen.contains(uniqueKey)) {
                bookingRepository.delete(booking);
            } else {
                seen.add(uniqueKey);
            }
        }
    }

    @Async
    public CompletableFuture<List<Booking>> scrapePage(String url) {
        try {
            List<Booking> scrapedBookings = new ArrayList<>();
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("div[data-cy='l-card']");
            for (Element element : elements) {
                Booking booking = new Booking();
                String title = element.select("h6.css-16v5mdi.er34gjf0").text();
                String priceString = element.select("p.css-tyui9s.er34gjf0").text();
                String locationDate = element.select("p.css-1a4brun.er34gjf0").text();
                String area = element.select("div.css-643j0o").text();
                String mainLink = element.select("a.css-z3gu2d").attr("href");

                // Check if priceString is empty
                if (priceString.isEmpty()) {
                    // Log or skip this element, as it doesn't have a price
                    continue;
                }

                // Removing non-numeric characters and parsing the price
                String priceValue = priceString.replaceAll("\\D+", "");
                if (priceValue.isEmpty()) {
                    // Log or skip this element, as the price could not be extracted
                    continue;
                }
                long tempPrice = Long.parseLong(priceValue);

                // Check if price falls within the desired range
                if (tempPrice >= 2000 && tempPrice <= 100000) {
                    booking.setTitle(title);
                    booking.setPrice(String.valueOf(tempPrice)); // Set price as string without currency
                    booking.setMainLink(transformLink(mainLink)); // Transform the link before setting
                    // Assuming location and date are separate fields in the Booking class
                    String[] parts = locationDate.split("-");
                    if (parts.length == 2) {
                        booking.setLocation(parts[0].trim());
                        booking.setDate(parts[1].trim());
                    }

                    // Check if booking already exists
                    if (!bookingExists(booking)) {
                        scrapedBookings.add(booking);
                        bookingRepository.save(booking); // Save only new bookings
                    }
                }
            }
            return CompletableFuture.completedFuture(scrapedBookings);
        } catch (IOException e) {
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }
    }

    private boolean bookingExists(Booking booking) {
        return bookingRepository.existsByTitleAndPrice(booking.getTitle(), booking.getPrice());
    }

    private String transformLink(String relativeLink) {
        return "https://www.olx.ua" + relativeLink;
    }

    public List<Booking> scrapeAndPersist(String baseUrl) throws InterruptedException, ExecutionException {
        try {
            Document doc = Jsoup.connect(baseUrl).get();
            // Find the pagination list
            Elements paginationItems = doc.select("li[data-testid='pagination-list-item']");
            // Find the last page number
            int totalPages = 1; // Default to 1 if no pagination is found
            if (!paginationItems.isEmpty()) {
                Element lastPaginationItem = paginationItems.last();
                totalPages = Integer.parseInt(lastPaginationItem.text());
            }

            List<CompletableFuture<List<Booking>>> futures = new ArrayList<>();
            for (int i = 1; i <= totalPages; i++) {
                String url = baseUrl + "?page=" + i;
                System.out.println("scraping page № - " + i);
                futures.add(scrapePage(url));
            }

            List<Booking> scrapedBookings = new ArrayList<>();
            for (CompletableFuture<List<Booking>> future : futures) {
                scrapedBookings.addAll(future.get());
            }

            List<Booking> newBookings = new ArrayList<>();
            for (Booking booking : scrapedBookings) {
                if (!bookingExists(booking.getTitle())) {
                    newBookings.add(booking);
                }
            }

            bookingRepository.saveAll(newBookings);
            return scrapedBookings;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList(); // Handle error appropriately
        }
    }

    private boolean bookingExists(String title) {
        return bookingRepository.existsByTitle(title);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getAllBookingsWithNonNullFields() {
        List<Booking> allBookings = bookingRepository.findAll();
        return allBookings.stream()
                .filter(booking -> booking.getTitle() != null && booking.getPrice() != null
                        && booking.getLocation() != null && booking.getDate() != null)
                .collect(Collectors.toList());
    }
}
