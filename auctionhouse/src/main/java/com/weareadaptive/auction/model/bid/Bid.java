package com.weareadaptive.auction.model.bid;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bid {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  private int quantity;

  private double price;

  private String state;

  private String username;

  private int auctionId;

  private int winQuantity;

  public int getQuantity() {
    return quantity;
  }

  public String getAuctionUser() {
    return username;
  }

  public double getPrice() {
    return price;
  }

  public int getWinQuantity() {
    return winQuantity;
  }

  public String getState() {
    return state;
  }

  public void setWin() {
    this.state = State.WIN.toString();
  }

  public void setLost() {
    this.state = State.LOST.toString();
  }

  public boolean isPending() {
    return this.state.equals(State.PENDING.toString());
  }

  public boolean hasWon() {
    return this.state.equals(State.PENDING.toString());
  }

  public boolean hasLost() {
    return this.state.equals(State.PENDING.toString());
  }

  @Override
  public String toString() {
    return "user=" + username
        + ", price=" + price
        + ", quantity=" + quantity;
  }

  public enum State {
    PENDING,
    LOST,
    WIN
  }
}
