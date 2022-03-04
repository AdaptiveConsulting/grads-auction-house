package com.weareadaptive.auction.service;

import com.weareadaptive.auction.controller.dto.BidAuctionRequest;
import com.weareadaptive.auction.controller.dto.NewBidResponse;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.AuctionState;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ObjectNotFoundException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import java.util.List;
import java.util.Optional;
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

  public AuctionLot create(String ownerName, String symbol, double minPrice, int quantity) {
    User owner = getUserByName(ownerName);

    var auctionLot = new AuctionLot(auctionState.nextId(), owner, symbol, quantity, minPrice);
    auctionState.add(auctionLot);

    return auctionLot;
  }

  public AuctionLot getById(int id) {
    AuctionLot auctionLot = auctionState.getAuctionIndex().get(id);

    if (auctionLot == null) {
      throw new ObjectNotFoundException("Auction with id " + id + " doesn't exist");
    }

    return auctionLot;
  }

  public AuctionState getAuctionState() {
    return auctionState;
  }

  public List<AuctionLot> getAll() {
    return auctionState.stream().toList();
  }

  public NewBidResponse bid(int id, BidAuctionRequest bidAuctionRequest, String username) {
    AuctionLot auctionLot = getById(id);

    if (auctionLot.getStatus() == AuctionLot.Status.CLOSED) {
      throw new BusinessException("Auction with ID " + id + " is closed");
    }

    if (auctionLot.getQuantity() > auctionLot.getQuantity()) {
      throw new BusinessException("User cannot bid more than the auction lot's quantity");
    }

    if (auctionLot.getMinPrice() < auctionLot.getMinPrice()) {
      throw new BusinessException("User cannot bid less than the auction lot's minimum price");
    }

    User bidder = getUserByName(username);

    auctionLot.bid(bidder, bidAuctionRequest.quantity(), bidAuctionRequest.price());

    return new NewBidResponse(
        auctionLot.getId(),
        auctionLot.getSymbol(),
        bidder.getUsername(),
        bidAuctionRequest.quantity(),
        bidAuctionRequest.price());
  }

  private User getUserByName(String name) {
    Optional<User> user = userState.getByUsername(name);

    if (user.isEmpty()) {
      throw new BusinessException("User " + name + "doesn't exist");
    }
    return user.get();
  }
}
