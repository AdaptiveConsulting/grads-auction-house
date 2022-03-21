package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;

public class AuctionBasicResponse {
  private final int id;
  private final String symbol;
  private final double minPrice;
  private final int quantity;
  private final String status;
  private Bid bid;

  public AuctionBasicResponse(int id, String symbol, double minPrice, int quantity,
                              String status) {
    this.id = id;
    this.symbol = symbol;
    this.minPrice = minPrice;
    this.quantity = quantity;
    this.status = status;
  }

  public AuctionBasicResponse(AuctionLot auctionLot, Bid bid) {
    this.id = auctionLot.getId();
    this.symbol = auctionLot.getSymbol();
    this.minPrice = auctionLot.getMinPrice();
    this.quantity = auctionLot.getQuantity();
    this.status = auctionLot.getStatus();
    this.bid = bid;
  }

  public int getId() {
    return id;
  }

  public String getSymbol() {
    return symbol;
  }

  public double getMinPrice() {
    return minPrice;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getStatus() {
    return status;
  }

  public Bid getBid() {
    return bid;
  }
}
