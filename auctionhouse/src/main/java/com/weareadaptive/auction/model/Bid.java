package com.weareadaptive.auction.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "AuctionBid")
public class Bid {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  private int auctionId;
  private int userId;
  private int quantity;
  private double price;
  private String state;
  private int winQuantity;

  public Bid() {
  }

  public Bid(int auctionId, int userId, int quantity, double price) {
    if (price <= 0) {
      throw new BusinessException("price must be above 0");
    }

    if (quantity <= 0) {
      throw new BusinessException("quantity must be above 0");
    }
    this.auctionId = auctionId;
    this.userId = userId;
    this.quantity = quantity;
    this.price = price;
    state = String.valueOf(State.PENDING);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getAuctionId() {
    return auctionId;
  }

  public void setAuctionId(int auctionId) {
    this.auctionId = auctionId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getState() {
    return state;
  }

  public void setState(State state) {
    this.state = String.valueOf(state);
  }

  public int getWinQuantity() {
    return winQuantity;
  }

  public void setWinQuantity(int winQuantity) {
    this.winQuantity = winQuantity;
  }

  @Override
  public String toString() {
    return "Bid{"
        + "userId=" + userId
        + ", price=" + price
        + ", quantity=" + quantity
        + '}';
  }

  public enum State {
    PENDING,
    LOST,
    WIN
  }
}
