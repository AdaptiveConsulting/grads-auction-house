package com.weareadaptive.auction.controller.dto;

public record BidInfo(int bidderId, int quantity, double price, String state,
                      int winQuantity
) {
}
