package com.weareadaptive.auction.service;

import com.weareadaptive.auction.exception.UnauthorizedException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.AuctionState;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ObjectNotFoundException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
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
    Optional<User> owner = userState.getByUsername(ownerName);

    if (owner.isEmpty()) {
      throw new BusinessException("owner name cannot be null");
    }

    var auctionLot = new AuctionLot(auctionState.nextId(), owner.get(), symbol, quantity, minPrice);
    auctionState.add(auctionLot);

    return auctionLot;
  }

  public AuctionLot getById(int id, String currentUsername) {
    Optional<AuctionLot> auctionLot = auctionState.stream()
        .filter(a -> a.getId() == id)
        .findFirst();

    if (auctionLot.isEmpty()) {
      throw new ObjectNotFoundException();
    }

    if (!auctionLot.get().getOwner().getUsername().equals(currentUsername)) {
      throw new UnauthorizedException("user is not authorized to access this auction");
    }

    return auctionLot.get();
  }

  public AuctionState getAuctionState() {
    return auctionState;
  }
}
