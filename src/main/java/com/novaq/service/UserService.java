package com.novaq.service;

import com.novaq.dtos.request.UserRegisterDTO;
import com.novaq.dtos.response.UserResponseDTO;
import com.novaq.enums.UserRole;
import com.novaq.model.User;
import com.novaq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(UserRegisterDTO register) {

        if (userRepository.findByEmail(register.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("User with the email address '%s' already exists.", register.getEmail())
            );
        }

        User createdUser = new User();
        createdUser.setEmail(register.getEmail());
        createdUser.setFullName(register.getName());
        createdUser.setPassword(passwordEncoder.encode(register.getPassword()));

        createdUser.getUserRole().add(UserRole.USER);

        User savedUser = userRepository.save(createdUser);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail()
        );
    }

}
