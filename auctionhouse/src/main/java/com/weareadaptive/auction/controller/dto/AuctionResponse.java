package com.weareadaptive.auction.controller.dto;

public record AuctionResponse(int id, String owner, String symbol, double minPrice, int quantity) {
}
