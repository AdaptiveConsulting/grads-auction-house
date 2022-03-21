package com.weareadaptive.auction.model.bid;

public record WonBid(
    int auctionLotId,
    String symbol,
    int wonQuantity,
    int bidQuantity,
    double price) {
}
