package com.weareadaptive.auction.dto.response;

import java.time.Instant;

public record AuctionResponse(
    int id,
    String owner,
    String symbol,
    double minPrice,
    int quantity,
    String status,
    Instant closeTimestamp) {

}

