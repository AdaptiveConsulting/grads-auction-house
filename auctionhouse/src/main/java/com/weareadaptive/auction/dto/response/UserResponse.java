package com.weareadaptive.auction.dto.response;

public record UserResponse(
        int id,
        String username,
        String firstName,
        String lastName,
        String organisation
) {
}

