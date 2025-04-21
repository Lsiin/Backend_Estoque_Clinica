package com.project.backend.repositories;


import com.project.backend.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductName(String productName);
    @Modifying
    @Query("DELETE FROM Stock s WHERE s.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

}
