package com.scrape.service;

import com.scrape.entity.Appartament;
import com.scrape.entity.Booking;
import com.scrape.repository.ApartamentRepository;
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

@Service
public class ApartamentService {

    @Autowired
    private ApartamentRepository apartamentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public String getMainLink(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.map(Booking::getMainLink).orElse(null);
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
            String link = mainLink;

            Appartament apartament = new Appartament();
            apartament.setTitle(title);
            apartament.setDescription(description);
            apartament.setPrice(price);
            apartament.setLocation(location);
            apartament.setArea(area);
            apartament.setMainLink(link);

            apartamentRepository.save(apartament);
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

    public List<Appartament> getAllAppartaments() {
        return apartamentRepository.findAll();
    }

    public List<String> getAllBookingLinks() {
        List<Booking> bookings = bookingRepository.findAll(); // Assuming findAll() returns all Booking entities

        // Extracting links from each Booking entity and storing them in a list
        List<String> bookingLinks = bookings.stream()
                .map(Booking::getMainLink)
                .collect(Collectors.toList());

        return bookingLinks;
    }
}