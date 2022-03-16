package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.Bid;
import java.util.List;

public class AuctionMapper {

  private AuctionMapper() {
  }

  public static List<BidInfo> mapAllBids(List<Bid> bids) {
    return bids.stream().map(bid -> new BidInfo(bid.getUserId(), bid.getQuantity(),
        bid.getPrice(), bid.getState(), bid.getWinQuantity())).toList();
  }


}
