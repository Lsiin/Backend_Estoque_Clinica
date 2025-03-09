package com.project.backend.repositories;

import com.project.backend.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Supplier, Long> {

}
