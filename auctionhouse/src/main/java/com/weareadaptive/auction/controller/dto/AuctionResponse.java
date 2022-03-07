package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import java.util.List;

public class AuctionResponse extends AuctionBasicResponse {
  private final List<Bid> bids;
  private ClosingSummary closingSummary;

  public AuctionResponse(AuctionLot auctionLot) {
    super(auctionLot.getId(), auctionLot.getSymbol(), auctionLot.getMinPrice(),
        auctionLot.getQuantity(), auctionLot.getStatus());
    this.bids = auctionLot.getBids();

    if (this.getStatus() == AuctionLot.Status.CLOSED) {
      this.closingSummary = auctionLot.getClosingSummary();
    }
  }

  public List<Bid> getBids() {
    return bids;
  }

  public ClosingSummary getClosingSummary() {
    return closingSummary;
  }
}
