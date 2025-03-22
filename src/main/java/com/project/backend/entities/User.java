package com.project.backend.entities;

import com.project.backend.exceptions.GlobalExceptionHandler;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Column(nullable = false)
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @Pattern(regexp = "\\d{3}.\\d{3}.\\d{3}-\\d{2}", message = "CPF must follow the pattern xxx.xxx.xxx-xx")
    @Column(nullable = false, unique = true)
    private String cpf;

    @NotNull(message = "Birthdate cannot be empty")
    @Column(nullable = false)
    private LocalDate birthday;


    @Pattern(
            regexp = "^\\+?\\d{0,2} \\(\\d{2}\\) \\d{4,5}-\\d{4}$",
            message = "The number must follow the pattern +XX (XX) XXXX-XXXX"
    )
    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phoneNumber;


        @NotNull(message = "Email cannot be null")
        @Column(nullable = false, unique = true)
        @Email(regexp = ".+@.+\\..+", message = "Email must be valid")
        private String email;


    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    @Pattern(regexp = "user|admin", message = "User type must be 'user' or 'admin'")
    private String userType;

}
