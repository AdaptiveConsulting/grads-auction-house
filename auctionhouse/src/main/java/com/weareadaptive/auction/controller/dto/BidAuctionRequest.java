package com.weareadaptive.auction.controller.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record BidAuctionRequest(
    @NotNull
    @Min(1)
    int quantity,

    @NotNull
    @DecimalMin("0.01")
    double price
) {
}
