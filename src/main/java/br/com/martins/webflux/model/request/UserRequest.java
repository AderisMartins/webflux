package br.com.martins.webflux.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest (

        @Size(min = 3, max = 50, message = "Deve conter entre 3 e 50 caracteres")
        @NotBlank(message = "Não pode ser vazio ou nulo")
        String name,

        @NotBlank(message = "Não pode ser vazio ou nulo")
        @Email(message = "Email inválido")
        String email,

        @Size(min = 3, max = 20, message = "Deve conter entre 3 e 20 caracteres")
        @NotBlank(message = "Não pode ser vazio ou nulo")
        String password
){}
