package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import java.util.ArrayList;
import java.util.List;

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

  public static ArrayList<AuctionResponse> mapAll(List<AuctionLot> auctions) {
    return new ArrayList<>(auctions.stream().map(AuctionMapper::map).toList());
  }
}
