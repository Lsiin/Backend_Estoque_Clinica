package com.project.backend.web.controller;

import com.project.backend.dto.AuthRequest;
import com.project.backend.dto.AuthResponse;
import com.project.backend.entities.Supplier;
import com.project.backend.entities.User;
import com.project.backend.jwt.JwtUtil;
import com.project.backend.jwt.TokenBlacklist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklist tokenBlacklist;


    @Operation(summary = "login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User connected successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "403", description = "Action unaltorized",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
         UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }

    @Operation(summary = "logout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User disconnected successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "403", description = "Action unaltorized",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.add(token);
            return ResponseEntity.ok("Logout realizado com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Token não encontrado no cabeçalho");
        }
    }
}
