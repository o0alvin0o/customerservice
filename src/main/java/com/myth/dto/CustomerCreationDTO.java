package com.myth.dto;

public record CustomerCreationDTO(
        String username,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
