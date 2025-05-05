package com.project.backend.web.controller;

import com.project.backend.entities.Product;
import com.project.backend.entities.User;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.repositories.UserRepository;
import com.project.backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;


@Tag(name = "user", description = "Contains any registers, searches, updates and deletes, of the users ")

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
     @Autowired
    private PasswordEncoder passwordEncoder;
     @Autowired
     private UserService userService;


    @Operation(summary = "Register a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registration successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Could not create user",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        if (!isValidEmail(user.getEmail())) {
            throw new GlobalExceptionHandler.InvalidEmailFormatException("Invalid Email");
        }
        if (!isValidCpf(user.getCpf())) {
            throw new GlobalExceptionHandler.InvalidCpfFormatException("Invalid CPF");
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            throw new GlobalExceptionHandler.InvalidPhoneNumberFormatException("Invalid Phone Number");
        }

        if (user.getCpf() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponses("CPF cannot be null"));
        }

        if (user.getPhoneNumber() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponses("Phone Number cannot be null"));
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponses("User name cannot be empty"));
        }

        if (user.getEmail() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponses("Email cannot be null"));
        }

        if (user.getBirthday() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponses("Birthday cannot be null"));
        }
        if (user.getUserType() == null) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("User type cannot be null");
        }
        try {
            User.UserType.valueOf(user.getUserType().name());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new GlobalExceptionHandler.EnumIncorretException("Invalid UserType: " + user.getUserType());
        }

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new GlobalExceptionHandler.DuplicateDataException("A user with this email already exists.");
        }

        Optional<User> existingUserByCpf = userRepository.findByCpf(user.getCpf());
        if (existingUserByCpf.isPresent()) {
            throw new GlobalExceptionHandler.DuplicateDataException("A user with this CPF already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(summary = "Find user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })



    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new GlobalExceptionHandler.UserNotFoundException("Usuário não encontrado com o ID:" + id);
        }
    }


    @Operation(summary = "Update a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User updated successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Could not update the user",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody @Valid User userDetails,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        return userRepository.findById(id)
                .map(existingUser -> {

                    boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                            .getAuthorities().stream()
                            .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));

                    boolean isSelf = existingUser.getEmail().equals(
                            SecurityContextHolder.getContext().getAuthentication().getName());

                    if (!isAdmin && !isSelf) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                "You can only update your own profile");
                    }

                    if (!isValidPhoneNumber(userDetails.getPhoneNumber())) {
                        throw new GlobalExceptionHandler.InvalidPhoneNumberFormatException("Invalid Phone Number");
                    }
                    if (!isValidEmail(userDetails.getEmail())) {
                        throw new GlobalExceptionHandler.InvalidEmailFormatException("Invalid Email");
                    }
                    if (!isValidCpf(userDetails.getCpf())) {
                        throw new GlobalExceptionHandler.InvalidCpfFormatException("Invalid CPF");
                    }

                    if (userDetails.getCpf() == null) {
                        return ResponseEntity.badRequest().body(new ErrorResponses("CPF cannot be null"));
                    }

                    if (userDetails.getPhoneNumber() == null) {
                        return ResponseEntity.badRequest().body(new ErrorResponses("Phone Number cannot be null"));
                    }

                    if (userDetails.getName() == null || userDetails.getName().isEmpty()) {
                        return ResponseEntity.badRequest().body(new ErrorResponses("User name cannot be empty"));
                    }

                    if (userDetails.getEmail() == null) {
                        return ResponseEntity.badRequest().body(new ErrorResponses("Email cannot be null"));
                    }

                    if (userDetails.getBirthday() == null) {
                        return ResponseEntity.badRequest().body(new ErrorResponses("Birthday cannot be null"));
                    }
                    if (userDetails.getUserType() == null) {
                        throw new GlobalExceptionHandler.ResourceNotFoundException("User type cannot be null");
                    }
                    if (!existingUser.getEmail().equals(userDetails.getEmail())) {
                        userRepository.findByEmail(userDetails.getEmail())
                                .ifPresent(u -> {
                                    throw new GlobalExceptionHandler.DuplicateDataException("Email already in use");
                                });
                    }

                    if (!existingUser.getCpf().equals(userDetails.getCpf())) {
                        userRepository.findByCpf(userDetails.getCpf())
                                .ifPresent(u -> {
                                    throw new GlobalExceptionHandler.DuplicateDataException("CPF already in use");
                                });
                    }


                    existingUser.setName(userDetails.getName());
                    existingUser.setCpf(userDetails.getCpf());
                    existingUser.setBirthday(userDetails.getBirthday());
                    existingUser.setPhoneNumber(userDetails.getPhoneNumber());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setPassword(userDetails.getPassword());
                    existingUser.setUserType(userDetails.getUserType());
                    userRepository.save(existingUser);

                    if (!existingUser.getEmail().equals(userDetails.getEmail())) {
                        existingUser.setEmail(userDetails.getEmail());
                    }

                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }

                    if (isAdmin) {
                        existingUser.setUserType(userDetails.getUserType());
                    }

                    return ResponseEntity.ok(userRepository.save(existingUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Delete a User",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User deleted successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })




    @PreAuthorize("hasRole('ADMIN')")

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new GlobalExceptionHandler.UserNotFoundException("User not found by id: " + id);
        }
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users= userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



    private boolean isValidEmail(String email) {
        return email.matches(".+@.+\\..+");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d{0,2} \\(\\d{2}\\) \\d{4,5}-\\d{4}$");
    }

    private boolean isValidCpf(String cpf) {
        return cpf.matches("\\d{3}.\\d{3}.\\d{3}-\\d{2}");
    }
}
