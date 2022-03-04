package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import java.util.List;

public class AuctionResponse {
  private final int id;
  private final String symbol;
  private final double minPrice;
  private final int quantity;
  private final List<Bid> bids;
  private final AuctionLot.Status status;
  private ClosingSummary closingSummary;


  public AuctionResponse(AuctionLot auctionLot) {
    this.id = auctionLot.getId();
    this.symbol = auctionLot.getSymbol();
    this.minPrice = auctionLot.getMinPrice();
    this.quantity = auctionLot.getQuantity();
    this.bids = auctionLot.getBids();
    this.status = auctionLot.getStatus();

    if (this.status == AuctionLot.Status.CLOSED) {
      ClosingSummary closingSummary = auctionLot.getClosingSummary();
    }
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

  public List<Bid> getBids() {
    return bids;
  }

  public AuctionLot.Status getStatus() {
    return status;
  }

  public ClosingSummary getClosingSummary() {
    return closingSummary;
  }
}
