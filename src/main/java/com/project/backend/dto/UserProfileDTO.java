package com.project.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {

    private String name;
    private String email;
    private String cpf;
    private String phone;
    private LocalDate birthDay;

    public UserProfileDTO( String name, String email, String cpf, String phone, LocalDate birthDay) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phone = phone;
        this.birthDay = birthDay;
    }
    }