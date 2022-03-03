package com.weareadaptive.auction.controller.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record CreateAuctionRequest(
    @NotBlank
    String symbol,

    @NotBlank
    @DecimalMin("0.01")
    double minPrice,

    @NotBlank
    @Min(1)
    int quantity
) {
}
