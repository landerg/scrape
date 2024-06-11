package com.scrape.service;

import com.scrape.entity.Apartment;
import com.scrape.entity.embeddable.ApartmentDescription;
import com.scrape.entity.Booking;
import com.scrape.entity.embeddable.Filter;
import com.scrape.repository.ApartmentRepository;
import com.scrape.repository.BookingRepository;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public String getMainLink(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.map(Booking::getMainLink).orElse(null);
    }

    private void populateApartmentDetails(Document doc, ApartmentDescription apartmentDescription, Filter filter) {
        // Populate apartmentDescription and filter fields here
    }

    public void scrapeDataFromLink(String mainLink) {
        try {
            Document doc = Jsoup.connect(mainLink).get();

            // Extracting data from the provided HTML structure
            String title = doc.select("div[class=swiper-slide-active] img").attr("alt");
            String description = doc.select("div[class=swiper-slide-active] img").attr("alt");
            String price = "price placeholder"; // Update this according to the actual HTML structure
            String location = "location placeholder"; // Update this according to the actual HTML structure
            String area = "area placeholder"; // Update this according to the actual HTML structure

            ApartmentDescription apartmentDescription = new ApartmentDescription();
            Filter filter = new Filter();
            populateApartmentDetails(doc, apartmentDescription, filter);

            Apartment apartment = new Apartment();
            apartment.setTitle(title);
            apartment.setDescription(description);
            apartment.setPrice(price);
            apartment.setLocation(location);
            apartment.setArea(area);
            apartment.setMainLink(mainLink);
            apartment.setApartmentDescription(apartmentDescription);
            apartment.setFilter(filter);

            apartmentRepository.save(apartment);
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 404) {
                System.out.println("URL not found: " + mainLink);
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public List<String> getAllBookingLinks() {
        List<Booking> bookings = bookingRepository.findAll(); // Assuming findAll() returns all Booking entities

        // Extracting links from each Booking entity and storing them in a list
        return bookings.stream()
                .map(Booking::getMainLink)
                .collect(Collectors.toList());
    }
}

