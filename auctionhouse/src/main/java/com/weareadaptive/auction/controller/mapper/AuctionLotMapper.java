package com.weareadaptive.auction.controller.mapper;

import com.weareadaptive.auction.controller.dto.auctions.AuctionLotResponse;
import com.weareadaptive.auction.controller.dto.auctions.ClosedAuctionLotResponse;
import com.weareadaptive.auction.controller.dto.auctions.OpenAuctionLotResponse;
import com.weareadaptive.auction.model.AuctionLot;

public class AuctionLotMapper {
  public static AuctionLotResponse map(AuctionLot auctionLot) {
    if (auctionLot.getStatus() == AuctionLot.Status.OPENED) {
      return new OpenAuctionLotResponse(
        auctionLot.getId(),
        auctionLot.getOwner().getUsername(),
        auctionLot.getSymbol(),
        auctionLot.getMinPrice(),
        auctionLot.getQuantity(),
        auctionLot.getStatus()
      );
    }
    return new ClosedAuctionLotResponse(
      auctionLot.getId(),
      auctionLot.getOwner().getUsername(),
      auctionLot.getSymbol(),
      auctionLot.getMinPrice(),
      auctionLot.getQuantity(),
      auctionLot.getStatus(),
      auctionLot.getClosingSummary().closingTime()
    );
  }
}
