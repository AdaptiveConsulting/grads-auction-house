package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;

public record AuctionBasicResponse(
    int id,
    String symbol,
    double minPrice,
    int quantity,
    AuctionLot.Status status
) {
}
