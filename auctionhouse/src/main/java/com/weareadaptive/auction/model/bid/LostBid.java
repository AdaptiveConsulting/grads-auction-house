package com.weareadaptive.auction.model.bid;

public record LostBid(int auctionLotId, String symbol, int quantity, double price) {
  @Override
  public String toString() {
    return "\n LostBid{"
        + "auctionLotId=" + auctionLotId
        + ", symbol='" + symbol
        + ", quantity=" + quantity
        + ", price=" + price
        + '}';
  }
}
