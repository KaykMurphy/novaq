package com.novaq.dtos.request;

import com.novaq.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class UserRegisterDTO {
    @NotBlank(message = "Name cannot be blank")
    private String nome;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String senha;

    @NotBlank(message = "Confirm Password is required")
    private String confirmSenha;

}
