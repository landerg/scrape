package com.scrape.scrape;

import com.scrape.entity.Booking;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Scraper {

    public List<Booking> scrape(String url) throws IOException {
        List<Booking> bookings = new ArrayList<>();

        // Parse the HTML content from the URL
        Document doc = Jsoup.connect(url).get();

        // Extract and group the elements
        Elements realEstateItems = doc.select("section.realty-item");
        for (Element realEstateItem : realEstateItems) {
            Booking booking = extractBookingData(realEstateItem);
            bookings.add(booking);
        }

        return bookings;
    }

    private Booking extractBookingData(Element realEstateItem) {
        Booking booking = new Booking();

        // Extract listing title
        booking.setTitle(realEstateItem.select("div.tit a.realty-link").text());

        // Extract location information
        Elements locationElements = realEstateItem.select("a[data-level]");
        for (Element element : locationElements) {
            String locationType = element.attr("data-level");
            String locationName = element.text();
            if (locationType.equals("area")) {
                booking.setLocation(locationName);
            } else if (locationType.equals("city")) {
                booking.setLocation(booking.getLocation() + ", " + locationName);
            }
        }

        // Extract property details
        Elements propertyDetailsElements = realEstateItem.select("div.chars span");
        for (Element element : propertyDetailsElements) {
            String detail = element.text();
            booking.setPropertyDetails(booking.getPropertyDetails() + detail + ", ");
        }

        // Extract description
        booking.setDescription(realEstateItem.select("div.desc-hidden").text());

        // Extract publication date
        booking.setPublicationDate(realEstateItem.select("time.withDate").text());

        // Extract main photo URL
        booking.setMainPhotoUrl(realEstateItem.select("div.main-photo a.realty-photo").select("img").attr("src"));

        // Extract small photo URLs
        Elements smallPhotoElements = realEstateItem.select("div.small-photo-wrap a.photo-small-photo");
        List<String> smallPhotoUrls = new ArrayList<>();
        for (Element element : smallPhotoElements) {
            String smallPhotoUrl = element.select("img").attr("src");
            smallPhotoUrls.add(smallPhotoUrl);
        }
        booking.setSmallPhotoUrls(smallPhotoUrls);

        // Extract price
        booking.setPrice(realEstateItem.select("div.flex.f-space.f-center b.size22").text());

        // Extract additional information (if any)
        // ...

        return booking;
    }
}
