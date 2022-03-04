package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;

public class AuctionMapper {
  public static AuctionResponse map(AuctionLot auctionLot) {
    return new AuctionResponse(
        auctionLot.getId(),
        auctionLot.getOwner().getUsername(),
        auctionLot.getSymbol(),
        auctionLot.getMinPrice(),
        auctionLot.getQuantity()
    );
  }
}
