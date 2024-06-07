package com.scrape.service;

import com.scrape.entity.Booking;
import com.scrape.entity.DeletedBooking;
import com.scrape.repository.BookingRepository;
import com.scrape.repository.DeletedBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataTransferService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DeletedBookingRepository deletedBookingRepository;

    public List<Booking> transferDataIfNotExists(Booking booking) {
        List<Booking> transferredBookings = new ArrayList<>();
        if (!bookingRepository.existsByTitle(booking.getTitle())) {
            DeletedBooking deletedBooking = new DeletedBooking();
            deletedBooking.setTitle(booking.getTitle());
            deletedBooking.setPrice(booking.getPrice());
            deletedBooking.setLocation(booking.getLocation());
            deletedBooking.setDate(booking.getDate());
            deletedBooking.setMainLink(booking.getMainLink());

            deletedBookingRepository.save(deletedBooking);
            bookingRepository.deleteByTitle(booking.getTitle());

            transferredBookings.add(booking);
        }
        return transferredBookings;
    }
}
