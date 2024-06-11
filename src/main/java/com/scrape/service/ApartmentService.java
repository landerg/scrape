package com.scrape.service;

import com.scrape.entity.Apartment;
import com.scrape.entity.embeddable.ApartmentDescription;
import com.scrape.entity.Booking;
import com.scrape.entity.embeddable.Filter;
import com.scrape.repository.ApartmentRepository;
import com.scrape.repository.BookingRepository;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.stream.Collectors;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;



@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public Optional<Apartment> findById(Long id) {
        // Call the appropriate method in your repository or DAO to retrieve the apartment by ID
        return apartmentRepository.findById(id);
    }

    public List<Apartment> scrapeApartments(String url) {
        List<Apartment> apartments = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements apartmentElements = doc.select("div.css-1wws9er");

            for (Element element : apartmentElements) {
                Apartment apartment = new Apartment();

                ApartmentDescription description = new ApartmentDescription();
                description.setDescription(element.select("div[data-cy=ad_description] div.css-1t507yq.er34gjf0").text());
                apartment.setApartmentDescription(description);

                Filter filter = new Filter();
                filter.setFloor(element.select("li:contains(Поверх)").text().replace("Поверх: ", ""));
                filter.setTotalArea(element.select("li:contains(Загальна площа)").text().replace("Загальна площа: ", ""));
                filter.setKitchenArea(element.select("li:contains(Площа кухні)").text().replace("Площа кухні: ", ""));
                filter.setWallType(element.select("li:contains(Тип стін)").text().replace("Тип стін: ", ""));
                filter.setRoomCount(element.select("li:contains(Кількість кімнат)").text().replace("Кількість кімнат: ", ""));
                filter.setBathroomType(element.select("li:contains(Cанвузол)").text().replace("Cанвузол: ", ""));
                filter.setHeatingType(element.select("li:contains(Опалення)").text().replace("Опалення: ", ""));
                filter.setRenovationType(element.select("li:contains(Ремонт)").text().replace("Ремонт: ", ""));
                filter.setFurnitureAvailability(element.select("li:contains(Меблювання)").text().replace("Меблювання: ", ""));
                filter.setAppliances(element.select("li:contains(Побутова техніка)").text().replace("Побутова техніка: ", ""));
                filter.setMultimedia(element.select("li:contains(Мультимедіа)").text().replace("Мультимедіа: ", ""));
                filter.setComfortOptions(element.select("li:contains(Комфорт)").text().replace("Комфорт: ", ""));
                filter.setCommunicationOptions(element.select("li:contains(Комунікації)").text().replace("Комунікації: ", ""));
                filter.setInfrastructure(element.select("li:contains(Інфраструктура)").text().replace("Інфраструктура: ", ""));

                apartment.setFilter(filter);

                // Save the apartment to the database
                apartmentRepository.save(apartment);

                apartments.add(apartment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apartments;
    }

    public void createApartmentFromBooking(Booking booking) {
        // Create a new Apartment object
        Apartment apartment = new Apartment();
        // Set fields from the Booking object
        apartment.setTitle(booking.getTitle());
        apartment.setPrice(booking.getPrice());
        apartment.setLocation(booking.getLocation());
        apartment.setDate(booking.getDate());
        apartment.setMainLink(booking.getMainLink());
        // Save the apartment to the database
        apartmentRepository.save(apartment);
    }

    public List<Apartment> getAllApartmentsWithNonNullFields() {
        List<Apartment> allApartments = apartmentRepository.findAll();
        return allApartments.stream()
                .filter(apartment -> apartment.getTitle() != null && apartment.getPrice() != null
                        && apartment.getLocation() != null && apartment.getDate() != null)
                .collect(Collectors.toList());
    }

    public void deleteDuplicateApartments() {
        List<Apartment> allApartments = apartmentRepository.findAll();
        Map<String, Map<String, Apartment>> apartmentsMap = new HashMap<>();

        // Group apartments by title and price
        for (Apartment apartment : allApartments) {
            String title = apartment.getTitle();
            String price = apartment.getPrice();

            if (!apartmentsMap.containsKey(title)) {
                apartmentsMap.put(title, new HashMap<>());
            }

            Map<String, Apartment> priceMap = apartmentsMap.get(title);
            priceMap.put(price, apartment);
        }

        // Delete duplicates
        for (Map.Entry<String, Map<String, Apartment>> entry : apartmentsMap.entrySet()) {
            Map<String, Apartment> priceMap = entry.getValue();

            if (priceMap.size() > 1) {
                // Remove duplicates by keeping only the first occurrence
                boolean first = true;
                for (Map.Entry<String, Apartment> priceEntry : priceMap.entrySet()) {
                    if (first) {
                        first = false;
                    } else {
                        // Delete duplicate apartment from the database
                        apartmentRepository.delete(priceEntry.getValue());
                    }
                }
            }
        }
    }
}

