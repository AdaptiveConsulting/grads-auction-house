package com.weareadaptive.auction.model;

public class WonBid {
  int auctionLotId;
  String symbol;
  int wonQuantity;
  int bidQuantity;
  double price;

  public WonBid() {
  }

  public WonBid(int auctionLotId, String symbol, int wonQuantity, int bidQuantity, double price) {
    this.auctionLotId = auctionLotId;
    this.symbol = symbol;
    this.wonQuantity = wonQuantity;
    this.bidQuantity = bidQuantity;
    this.price = price;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getWonQuantity() {
    return wonQuantity;
  }

  public void setWonQuantity(int wonQuantity) {
    this.wonQuantity = wonQuantity;
  }

  public int getBidQuantity() {
    return bidQuantity;
  }

  public void setBidQuantity(int bidQuantity) {
    this.bidQuantity = bidQuantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }
}