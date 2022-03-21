package com.weareadaptive.auction.dto;

import com.weareadaptive.auction.dto.response.AuctionResponse;
import com.weareadaptive.auction.dto.response.BidResponse;
import com.weareadaptive.auction.dto.response.UserResponse;
import com.weareadaptive.auction.model.auction.Auction;
import com.weareadaptive.auction.model.bid.Bid;
import com.weareadaptive.auction.model.user.AuctionUser;

public class Mapper {
  private Mapper() {
  }

  public static UserResponse map(AuctionUser auctionUser) {
    return new UserResponse(
        auctionUser.getId(),
        auctionUser.getUsername(),
        auctionUser.getFirstName(),
        auctionUser.getLastName(),
        auctionUser.getOrganization());
  }

  public static AuctionResponse map(Auction auction) {
    return new AuctionResponse(
        auction.getId(),
        auction.getOwner(),
        auction.getSymbol(),
        auction.getMinPrice(),
        auction.getQuantity(),
        auction.getStatus(),
        auction.getCloseTimestamp()
    );
  }


  public static BidResponse map(Bid bid) {
    return new BidResponse(
        bid.getUsername(),
        bid.getState(),
        bid.getQuantity(),
        bid.getPrice()
    );
  }


}
