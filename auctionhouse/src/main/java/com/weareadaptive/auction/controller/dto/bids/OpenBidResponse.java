package com.weareadaptive.auction.controller.dto.bids;

import com.weareadaptive.auction.model.Bid;

public class OpenBidResponse extends BidResponse {
  public OpenBidResponse(String username, int quantity, double price, Bid.State state) {
    super(username, quantity, price, state);
  }
}
