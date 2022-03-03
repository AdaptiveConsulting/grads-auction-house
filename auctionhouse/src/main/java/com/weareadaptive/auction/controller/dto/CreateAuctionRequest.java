package com.weareadaptive.auction.controller.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateAuctionRequest(
    @NotBlank
    String symbol,

    @NotNull
    @DecimalMin("0.01")
    double minPrice,

    @NotNull
    @Min(1)
    int quantity
) {
}
