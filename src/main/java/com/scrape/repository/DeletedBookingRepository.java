package com.scrape.repository;

import com.scrape.entity.DeletedBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedBookingRepository extends JpaRepository<DeletedBooking, Long> {
    // Add custom query methods if needed
}
