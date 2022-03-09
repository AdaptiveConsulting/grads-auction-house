package com.weareadaptive.auction.service;

import static java.lang.String.format;

import com.weareadaptive.auction.model.AuctionState;
import org.springframework.stereotype.Service;

@Service
public class AuctionLotService {
  public static final String AUCTION_LOT_ENTITY = "AuctionLot";
  private final AuctionState auctionState;
  private final UserState userState;

  public AuctionLotService(AuctionState auctionState, UserState userState) {
    this.auctionState = auctionState;
    this.userState = userState;
  }
}
