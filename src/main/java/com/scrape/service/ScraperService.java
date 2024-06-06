package com.scrape.service;

import com.scrape.entity.Booking;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {

    public List<Booking> scrapeAllPages(String baseUrl) throws IOException {
        List<Booking> allBookings = new ArrayList<>();
        int currentPage = 1;
        int totalPages = getTotalPages(baseUrl);

        while (currentPage <= totalPages) {
            System.out.println("Scraping page " + currentPage + " of " + totalPages + "...");
            String url = baseUrl + "?page=" + currentPage;
            List<Booking> pageBookings = scrapePage(url);
            allBookings.addAll(pageBookings);
            currentPage++;
        }

        System.out.println("Scraping complete. Total pages scraped: " + (currentPage - 1));
        return allBookings;
    }

    private int getTotalPages(String baseUrl) throws IOException {
        Document doc = Jsoup.connect(baseUrl).get();
        Element paginationLink = doc.selectFirst("[data-testid=pagination-link-25]");
        if (paginationLink != null) {
            String lastPageUrl = paginationLink.attr("href");
            return Integer.parseInt(lastPageUrl.substring(lastPageUrl.lastIndexOf('=') + 1));
        }
        return 1; // If pagination link is not found, assume only one page
    }

    private List<Booking> scrapePage(String url) throws IOException {
        List<Booking> bookings = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("div[data-cy=l-card]");

        for (Element element : elements) {
            Long id = Long.valueOf(element.attr("id")); // Parse id as Long
            String title = element.select("h6").text();
            String price = element.select("p[data-testid=ad-price]").text();
            String locationDate = element.select("p[data-testid=location-date]").text();
            String area = element.select("span.css-643j0o").text();
            String mainLink = "https://www.olx.ua" + element.select("a.css-z3gu2d").attr("href"); // Construct main link

            // Extracting location and date from the combined string
            String[] parts = locationDate.split("-");
            String location = parts[0].trim();
            String date = parts[1].trim();

            Booking booking = new Booking(id, title, price, location, date, area, mainLink);
            bookings.add(booking);
        }

        return bookings;
    }
}