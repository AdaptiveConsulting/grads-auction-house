package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;

public class AuctionBasicResponse {
  private final int id;
  private final String symbol;
  private final double minPrice;
  private final int quantity;
  private final AuctionLot.Status status;

  public AuctionBasicResponse(int id, String symbol, double minPrice, int quantity,
                              AuctionLot.Status status) {
    this.id = id;
    this.symbol = symbol;
    this.minPrice = minPrice;
    this.quantity = quantity;
    this.status = status;
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

  public AuctionLot.Status getStatus() {
    return status;
  }
}
