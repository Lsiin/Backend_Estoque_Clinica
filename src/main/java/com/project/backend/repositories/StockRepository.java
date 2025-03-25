package com.project.backend.repositories;

import com.project.backend.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductName(String productName);
    Optional<Stock> findByProductId(Long productId);

}
