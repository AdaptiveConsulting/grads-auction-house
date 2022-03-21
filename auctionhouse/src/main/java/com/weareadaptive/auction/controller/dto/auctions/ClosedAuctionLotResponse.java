package com.weareadaptive.auction.controller.dto.auctions;

import com.weareadaptive.auction.model.AuctionLot;
import java.time.Instant;

public class ClosedAuctionLotResponse extends AuctionLotResponse {
  private final Instant closedTime;

  public ClosedAuctionLotResponse(
      int id,
      String owner,
      String symbol,
      double minPrice,
      int quantity,
      AuctionLot.Status status,
      Instant closedTime) {
    super(id, owner, symbol, minPrice, quantity, status);
    this.closedTime = closedTime;
  }

  public Instant getClosedTime() {
    return closedTime;
  }
}
