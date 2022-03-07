package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
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

  public static List<BidInfo> mapAllBids(List<Bid> bids) {
    return bids.stream().map(bid -> new BidInfo(bid.getUser().getUsername(), bid.getQuantity(),
        bid.getPrice(), bid.getState(), bid.getWinQuantity())).toList();
  }
}
