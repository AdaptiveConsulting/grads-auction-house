package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.Bid;

public record BidInfo(String bidder, int quantity, double price, Bid.State state,
                      int winQuantity
) {
}
