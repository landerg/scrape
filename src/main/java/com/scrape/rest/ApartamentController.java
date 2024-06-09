package com.scrape.rest;

import com.scrape.entity.Appartament;
import com.scrape.service.ApartamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ApartamentController {

    @Autowired
    private ApartamentService apartamentService;

    @GetMapping("/scrapeAll")
    public String scrapeAll(Model model) {
        List<String> bookingLinks = apartamentService.getAllBookingLinks();

        for (String bookingLink : bookingLinks) {
            apartamentService.scrapeDataFromLink(bookingLink);
        }

        return "redirect:/appartaments";
    }

    @GetMapping("/appartaments")
    public String getAppartaments(Model model) {
        List<Appartament> appartaments = apartamentService.getAllAppartaments();
        model.addAttribute("appartaments", appartaments);
        return "appartaments";
    }
}