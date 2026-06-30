package com.novaq.config;

import com.novaq.enums.UserRole;
import com.novaq.model.User;
import com.novaq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Value("${admin_password}")
    private String password;

    @Value("${email_admin}")
    private String emailAdmin;

    @Override
    public void run(String... args) throws Exception {

        boolean adminExists = userRepository.existsByEmail(emailAdmin);

        if (!adminExists){
            log.info("Admin user not created. Creating...");

            User user = new User();
            user.setEmail(emailAdmin);
            user.setFullName("Admin NOVAq");
            user.setPassword(passwordEncoder.encode(password));
            user.getUserRole().add(UserRole.ADMIN);

            userRepository.save(user);

            log.info("Default admin user created successfully.");
        }

    }
}
