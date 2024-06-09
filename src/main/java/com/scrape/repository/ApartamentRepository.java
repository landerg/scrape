package com.scrape.repository;

import com.scrape.entity.Appartament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartamentRepository extends JpaRepository<Appartament, Long> {
}
