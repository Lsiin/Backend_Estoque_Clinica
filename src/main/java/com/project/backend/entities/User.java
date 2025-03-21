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
@Table(name = "users") // nome da tabela no banco
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

    @Column(nullable = false, unique = true)
    @Email(regexp = ".+@.+\\..+", message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Pattern(regexp = "user|admin", message = "User type must be 'user' or 'admin'")
    private String userType;

    // Validadores com exceções personalizadas
    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidCpfFormatException("CPF cannot be null");
        }
        if (!cpf.matches("\\d{3}.\\d{3}.\\d{3}-\\d{2}")) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidCpfFormatException("CPF is invalid");
        }
        this.cpf = cpf;
    }


    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidPhoneNumberFormatException("Phone number cannot be null");
        }
        if (!phoneNumber.matches("^\\+?\\d{0,2} \\(\\d{2}\\) \\d{4,5}-\\d{4}$")) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidPhoneNumberFormatException("Phone number is invalid");
        }
        this.phoneNumber = phoneNumber;
    }
}
