package com.weareadaptive.auction.service;

import com.weareadaptive.auction.exception.KeyDoesNotExistException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.AuctionState;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.UserState;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class AuctionLotService {
  private final AuctionState auctionState;
  private final UserState userState;

  public AuctionLotService(AuctionState auctionState, UserState userState) {
    this.auctionState = auctionState;
    this.userState = userState;
  }

  public AuctionLot createAuctionLot(
      String username,
      String symbol,
      double minPrice,
      int quantity) {
    AuctionLot auctionLot = new AuctionLot(
        auctionState.nextId(),
        userState.getByUsername(username)
          .orElseThrow(() -> new KeyDoesNotExistException("User does not exist")),
        symbol,
        quantity,
        minPrice);
    auctionState.add(auctionLot);
    return auctionLot;
  }

  public AuctionLot getAuctionLot(int id) {
    return auctionState.get(id);
  }

  public Stream<AuctionLot> getAllAuctionLots() {
    return auctionState.stream();
  }

  public Bid createBid(String username, int id, double price, int quantity) {
    auctionState.get(id)
        .bid(
          userState.getByUsername(username)
            .orElseThrow(() -> new KeyDoesNotExistException("User does not exist")),
          quantity,
          price
        );
    int lastBid = auctionState.get(id)
        .getBids()
        .size() - 1;
    return auctionState.get(id)
      .getBids()
      .get(lastBid);
  }

  public List<Bid> getBids(String username, int id) {
    if (!auctionState.get(id)
        .getOwner()
        .getUsername()
        .equals(username)) {
      throw new BusinessException("User is not the owner of AuctionLot " + id);
    }
    return auctionState.get(id)
      .getBids();
  }

  public ClosingSummary close(String username, int id) {
    if (!auctionState.get(id)
        .getOwner()
        .getUsername()
        .equals(username)) {
      throw new BusinessException("User is not the owner of AuctionLot " + id);
    }
    auctionState.get(id).close();
    return auctionState.get(id)
      .getClosingSummary();
  }

  public ClosingSummary getClosingSummary(int id) {
    return auctionState.get(id)
      .getClosingSummary();
  }
}
