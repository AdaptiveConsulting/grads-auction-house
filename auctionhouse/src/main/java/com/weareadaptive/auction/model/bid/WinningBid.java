package com.weareadaptive.auction.model.bid;

public record WinningBid(int quantity, Bid originalBid) {
  @Override
  public String toString() {
    return "\nWinningBid{"
        + "quantity=" + quantity
        + ", originalBid=" + originalBid
        + '}';
  }
}

