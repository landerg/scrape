package com.scrape.rest;

import com.scrape.entity.Apartment;
import com.scrape.entity.Booking;
import com.scrape.service.ApartmentService;
import com.scrape.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/scrapeAll")
    public String scrapeAll() {
        // Scraping and creating apartments from bookings
        List<Booking> apartments = bookingService.getAllBookingsWithNonNullFields();
        for (Booking apartment : apartments) {
            apartmentService.createApartmentFromBooking(apartment);
        }

        // Remove duplicates from the database
        apartmentService.deleteDuplicateApartments();

        // Redirect to the apartments view
        return "redirect:/apartments";
    }


    @GetMapping("/apartments")
    public String getApartments(Model model) {
        // Get all apartments with not null fields from the database
        List<Apartment> apartments = apartmentService.getAllApartmentsWithNonNullFields();
        model.addAttribute("apartments", apartments);
        return "apartments";
    }

    @GetMapping("/scrapeById/{id}")
    public ModelAndView scrapeById(@PathVariable Long id) {
        Optional<Apartment> optionalApartment = apartmentService.findById(id);
        if (optionalApartment.isPresent()) {
            Apartment apartment = optionalApartment.get();
            String url = apartment.getMainLink();
            List<Apartment> scrapedApartments = apartmentService.scrapeApartments(url);

            ModelAndView modelAndView = new ModelAndView("scrapeResult");
            modelAndView.addObject("apartments", scrapedApartments);
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/error");
        }
    }
}