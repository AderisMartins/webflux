package br.com.martins.webflux.model.response;

public record UserResponse(
    String id,
    String name,
    String email,
    String password
) { }
