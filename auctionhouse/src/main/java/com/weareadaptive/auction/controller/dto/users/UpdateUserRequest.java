package com.weareadaptive.auction.controller.dto.users;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UpdateUserRequest(
    @NotBlank
    @Size(max = 100)
    String firstName,

    @NotBlank
    @Size(max = 100)
    String lastName,

    @NotBlank
    @Size(max = 100)
    String organisation) {
}
