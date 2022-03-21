package com.weareadaptive.auction.controller.dto.users;

public record UserResponse(
        int id,
        String username,
        String firstName,
        String lastName,
        String organisation
) {
}

