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
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "Name cannot be null")
        @Column(nullable = false)
        @Size(min = 3, message = "Name must be at least 3 characters long")
        private String name;

        @Pattern(regexp = "\\d{3}.\\d{3}.\\d{3}-\\d{2}",message = "CPF must follow the pattern xxx.xxx.xxx-xx")
        @Column(nullable = false,unique = true)
        private String cpf;

        @NotNull(message = "Birthdate cannot be empty")
        @Column(nullable = false)
        private LocalDate birthday;

        @NotNull(message = "Cep cannot be null")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP must follow the pattern xxxxx-xxx")
        @Column(nullable = false)
        private String cep;

        @Pattern(regexp="\"^\\\\+?\\\\d{0,2} \\\\(\\\\d{2}\\\\) \\\\d{4,5}-\\\\d{4}$\"", message = "The number is incorrect pattern ")
        @NotBlank(message=" Phone number is required")
        @Column(nullable = false)
        private String phoneNumber;

        @Column(nullable = false, unique = true)
        @Email(regexp = ".+@.+\\..+", message = "Email must be valid")
        private String email;

        @NotBlank(message = "Password is required")
        @Column(nullable = false)
        private String password;


    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidCpfFormatException("CPF cannot be null");
        }
        if (!cpf.matches("\\d{3}.\\d{3}.\\d{3}-\\d{2}")) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidCpfFormatException("CPF is invalid");
        }
        this.cpf = cpf;
    }
    public void setCep(String cep) {
        if (cep == null) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidCepFormatException("CEP cannot be null");
        }
        if (!cep.matches("\\d{5}-\\d{3}")) {
            throw new GlobalExceptionHandler.UserNotFoundException.InvalidCepFormatException("CEP is invalid");
        }
        this.cep = cep;
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



