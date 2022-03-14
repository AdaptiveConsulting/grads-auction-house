package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import java.util.List;

public class AuctionResponse extends AuctionBasicResponse {
  private final List<Bid> bids;

  public AuctionResponse(AuctionLot auctionLot, List<Bid> bids) {
    super(auctionLot.getId(), auctionLot.getSymbol(), auctionLot.getMinPrice(),
        auctionLot.getQuantity(), auctionLot.getStatus());
    this.bids = bids;
  }

  public List<Bid> getBids() {
    return bids;
  }
}
