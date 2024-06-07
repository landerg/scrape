package com.scrape.repository;


import com.scrape.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    //void save(Booking booking);
    List<Booking> findAll();
    List<Booking> findByTitle(String title);

    List<Booking> findByTitleAndPrice(String title, String price);

    boolean existsByTitle(String title);

    void deleteByTitle(String title);
}
