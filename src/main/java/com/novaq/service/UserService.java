package com.novaq.service;

import com.novaq.dtos.request.UserRegisterDTO;
import com.novaq.dtos.response.UserResponseDTO;
import com.novaq.enums.UserRole;
import com.novaq.model.User;
import com.novaq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

        User userCriado = new User();
        userCriado.setEmail(register.getEmail());
        userCriado.setNomeCompleto(register.getNome());
        userCriado.setSenha(passwordEncoder.encode(register.getSenha()));

        userCriado.getUserRole().add(UserRole.USER);

        User userSalvo = userRepository.save(userCriado);

        return new UserResponseDTO(
                userSalvo.getId(),
                userSalvo.getNomeCompleto(),
                userSalvo.getEmail()
        );
    }

}
