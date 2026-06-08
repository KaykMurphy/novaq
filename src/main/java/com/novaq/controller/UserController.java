package com.novaq.controller;

import com.novaq.dtos.request.UserLoginDTO;
import com.novaq.dtos.request.UserRegisterDTO;
import com.novaq.dtos.response.TokenResponseDTO;
import com.novaq.dtos.response.UserResponseDTO;
import com.novaq.model.User;
import com.novaq.service.CustomUserDetails;
import com.novaq.service.TokenService;
import com.novaq.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterDTO register){
        UserResponseDTO response = userService.register(register);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody UserLoginDTO login){

        var usernamePassword = new UsernamePasswordAuthenticationToken(login.email(), login.senha());

        var auth = authenticationManager.authenticate(usernamePassword);

        //retrieves authenticated user from auth object
        var customUserDetails = (CustomUserDetails) auth.getPrincipal();

        String token = tokenService.generateToken(customUserDetails.getUser());

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }


}
