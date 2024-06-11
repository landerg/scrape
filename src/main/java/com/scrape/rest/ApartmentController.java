package com.scrape.rest;

import com.scrape.entity.Apartment;
import com.scrape.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("/scrapeAll")
    public String scrapeAll(Model model) {
        List<String> bookingLinks = apartmentService.getAllBookingLinks();

        for (String bookingLink : bookingLinks) {
            apartmentService.scrapeDataFromLink(bookingLink);
        }

        return "redirect:/apartments";
    }

    @GetMapping("/apartments")
    public String getApartments(Model model) {
        List<Apartment> apartments = apartmentService.getAllApartments();
        model.addAttribute("apartments", apartments);
        return "apartments";
    }
}