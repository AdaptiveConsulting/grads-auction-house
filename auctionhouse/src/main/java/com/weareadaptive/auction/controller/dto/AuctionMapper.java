package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import java.util.List;

public class AuctionMapper {

  private AuctionMapper() {
  }

  public static AuctionResponse map(AuctionLot auctionLot, List<Bid> bids) {
    return new AuctionResponse(auctionLot, bids);
  }

  public static AuctionBasicResponse mapBasicInfo(AuctionLot auctionLot, Bid bid) {
    return new AuctionBasicResponse(auctionLot, bid);
  }

  public static List<BidInfo> mapAllBids(List<Bid> bids) {
    return bids.stream().map(bid -> new BidInfo(bid.getUserId(), bid.getQuantity(),
        bid.getPrice(), bid.getState(), bid.getWinQuantity())).toList();
  }


}
