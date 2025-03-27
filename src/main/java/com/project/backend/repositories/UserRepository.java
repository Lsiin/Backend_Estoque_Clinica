package com.project.backend.repositories;


import com.project.backend.entities.User;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@NonNullApi
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByCpf(String cpf);
    Optional<User> existsByCpf(String cpf);
    Optional<User> existsByEmail(String email);
}