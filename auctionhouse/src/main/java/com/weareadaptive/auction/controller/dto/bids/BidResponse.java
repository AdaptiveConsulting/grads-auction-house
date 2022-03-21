package com.weareadaptive.auction.controller.dto.bids;

import com.weareadaptive.auction.model.Bid;

public class BidResponse {
  private final String username;
  private final int quantity;
  private final double price;
  private final Bid.State state;

  public BidResponse(String username, int quantity, double price, Bid.State state) {
    this.username = username;
    this.quantity = quantity;
    this.price = price;
    this.state = state;
  }

  public String getUsername() {
    return username;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return price;
  }

  public Bid.State getState() {
    return state;
  }
}
