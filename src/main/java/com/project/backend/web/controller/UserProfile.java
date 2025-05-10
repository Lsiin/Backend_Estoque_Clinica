package com.project.backend.web.controller;

import com.project.backend.dto.UserProfileDTO;
import com.project.backend.entities.User;
import com.project.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserProfile {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UserProfileDTO profile = new UserProfileDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getPhoneNumber(),
                user.getBirthday()
        );
        return ResponseEntity.ok(profile);
    }

}
