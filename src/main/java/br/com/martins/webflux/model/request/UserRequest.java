package br.com.martins.webflux.model.request;

public record UserRequest (

    String name,
    String email,
    String password
){}
