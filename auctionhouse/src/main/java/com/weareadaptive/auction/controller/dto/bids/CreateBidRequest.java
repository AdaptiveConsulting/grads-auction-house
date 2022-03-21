package com.weareadaptive.auction.controller.dto.bids;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record CreateBidRequest(
    @NotNull
    @Positive
    int quantity,

    @NotNull
    @Positive
    double price
) {
}
