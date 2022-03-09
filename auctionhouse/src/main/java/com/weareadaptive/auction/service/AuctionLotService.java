package com.weareadaptive.auction.service;

import com.weareadaptive.auction.controller.dto.BidAuctionRequest;
import com.weareadaptive.auction.controller.dto.NewBidResponse;
import com.weareadaptive.auction.exception.UnauthorizedException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.AuctionState;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.ObjectNotFoundException;
import com.weareadaptive.auction.model.User;
import java.security.Principal;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public record AuctionLotService(AuctionState auctionState,
                                UserService userService) {
  public AuctionLot create(String ownerName, String symbol, double minPrice, int quantity) {
    var owner = getUserByName(ownerName);

    var auctionLot = new AuctionLot(auctionState.nextId(), owner, symbol, quantity, minPrice);
    auctionState.add(auctionLot);

    return auctionLot;
  }

  public AuctionLot getById(int id) {
    var auctionLot = auctionState.getAuctionIndex().get(id);

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
    var auctionLot = getById(id);

    if (auctionLot.getStatus() == AuctionLot.Status.CLOSED) {
      throw new BusinessException("Auction with ID " + id + " is closed");
    }

    var bidder = getUserByName(username);

    auctionLot.bid(bidder, bidAuctionRequest.quantity(), bidAuctionRequest.price());

    return new NewBidResponse(
        auctionLot.getId(),
        auctionLot.getSymbol(),
        bidder.getUsername(),
        bidAuctionRequest.quantity(),
        bidAuctionRequest.price());
  }

  private User getUserByName(String name) {
    throw new UnsupportedOperationException();
    /*
    var user = userService.getByUsername(name);

    if (user.isEmpty()) {
      throw new BusinessException("User " + name + "doesn't exist");
    }
    return user.get();

     */
  }

  public List<Bid> getBids(int auctionId, Principal principal) {
    var auctionLot = getById(auctionId);
    verifyOwnership(auctionLot, principal.getName());

    return getById(auctionId).getBids();
  }

  public ClosingSummary close(int auctionId, Principal principal) {
    var auctionLot = getById(auctionId);
    verifyOwnership(auctionLot, principal.getName());
    auctionLot.close();

    return auctionLot.getClosingSummary();
  }

  public ClosingSummary getSummary(int auctionId, Principal principal) {
    var auctionLot = getById(auctionId);
    verifyOwnership(auctionLot, principal.getName());

    return auctionLot.getClosingSummary();
  }

  private void verifyOwnership(AuctionLot auctionLot, String username) {
    if (!auctionLot.isOwner(username)) {
      throw new UnauthorizedException("The user is not the owner of auction lot.");
    }
  }
}
