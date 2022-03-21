package com.weareadaptive.auction.controller.dto.auctions;

import com.weareadaptive.auction.controller.dto.auctions.AuctionLotResponse;
import com.weareadaptive.auction.model.AuctionLot;

public class OpenAuctionLotResponse extends AuctionLotResponse {
  public OpenAuctionLotResponse(
      int id,
      String owner,
      String symbol,
      double minPrice,
      int quantity,
      AuctionLot.Status status) {
    super(id, owner, symbol, minPrice, quantity, status);
  }
}
