package com.payme.gateway.dto;

public record RegisterDto(
        String firstName,
        String lastName,
        String username,
        String email,
        String password) {

}