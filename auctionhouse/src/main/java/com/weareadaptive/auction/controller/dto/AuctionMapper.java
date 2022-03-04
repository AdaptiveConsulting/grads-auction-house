package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import java.util.List;

public class AuctionMapper {
  private AuctionMapper() {
  }

  public static AuctionResponse map(AuctionLot auctionLot) {
    return new AuctionResponse(auctionLot);
  }

  public static List<AuctionResponse> mapAll(List<AuctionLot> auctions) {
    return auctions.stream().map(AuctionMapper::map).toList();
  }

  public static AuctionBasicResponse mapBasicInfo(AuctionLot auctionLot) {
    return new AuctionBasicResponse(
        auctionLot.getId(),
        auctionLot.getSymbol(),
        auctionLot.getMinPrice(),
        auctionLot.getQuantity(),
        auctionLot.getStatus()
    );
  }
}
