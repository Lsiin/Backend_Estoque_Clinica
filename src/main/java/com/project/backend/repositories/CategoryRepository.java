package com.project.backend.repositories;

import com.project.backend.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByName(String name);

}
