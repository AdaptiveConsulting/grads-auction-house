package com.weareadaptive.auction.controller.dto.auctions;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record CreateAuctionLotRequest(
    @NotBlank
    @Size(max = 100)
    String symbol,

    @NotNull
    @Positive
    double minPrice,

    @NotNull
    @Positive
    int quantity
) {
}
