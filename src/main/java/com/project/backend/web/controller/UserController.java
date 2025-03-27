package com.project.backend.web.controller;


import com.project.backend.entities.User;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.Optional;



@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Working");
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ErrorResponses(errorMessage));
        }


        if (!isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponses("Invalid email"));
        }
        if (!isvalidCpf(user.getCpf())) {
            return ResponseEntity.badRequest().body(new ErrorResponses("Invalid CPF"));
        }
        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new ErrorResponses("Invalid Phone Number"));
        }
        if (user.getBirthday() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponses("Birthday cannot be null"));
        }


        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new GlobalExceptionHandler.DuplicateDataException("A user with this email already exists.");
        }
        Optional<User> existingUserByCpf = userRepository.findByCpf(user.getCpf());
        if (existingUserByCpf.isPresent()) {
            throw new GlobalExceptionHandler.DuplicateDataException("A user with this CPF already exists.");
        }


        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }else {
            throw new GlobalExceptionHandler.UserNotFoundException("Usuário não encontrado com o ID:" + id);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (existingUser.getCpf() != null) {
                        existingUser.setCpf(userDetails.getCpf());
                    }
                    existingUser.setName(userDetails.getName());
                    existingUser.setBirthday(userDetails.getBirthday());
                    existingUser.setPhoneNumber(userDetails.getPhoneNumber());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setPassword(userDetails.getPassword());
                    User updateUser = userRepository.save(existingUser);
                    return ResponseEntity.ok(updateUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found by id: " + id);
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches(".+@.+\\..+");
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d{0,2} \\(\\d{2}\\) \\d{4,5}-\\d{4}$");
    }

    private boolean isvalidCpf(String cpf) {
        return cpf.matches("\\d{3}.\\d{3}.\\d{3}-\\d{2}");
    }
}

