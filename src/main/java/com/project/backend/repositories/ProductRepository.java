package com.project.backend.repositories;

import com.project.backend.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findById(Long id);
}
