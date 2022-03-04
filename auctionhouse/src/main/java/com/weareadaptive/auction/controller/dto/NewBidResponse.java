package com.weareadaptive.auction.controller.dto;

public record NewBidResponse(
    int auctionId, String symbol, String bidderName, int bidQuantity, double bidPrice) {
}
