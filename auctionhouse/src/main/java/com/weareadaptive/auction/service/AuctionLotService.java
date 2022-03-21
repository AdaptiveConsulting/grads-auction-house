package com.weareadaptive.auction.service;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.math.BigDecimal.valueOf;

import com.weareadaptive.auction.controller.dto.NewBidResponse;
import com.weareadaptive.auction.exception.UnauthorizedException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.ObjectNotFoundException;
import com.weareadaptive.auction.repository.AuctionRepository;
import com.weareadaptive.auction.repository.BidRepository;
import com.weareadaptive.auction.repository.UserRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public record AuctionLotService(AuctionRepository auctionRepository, UserRepository userRepository,
                                BidRepository bidRepository) {

  public AuctionLot create(String ownerName, String symbol, double minPrice, int quantity) {
    var owner = userRepository.findByName(ownerName);

    var auctionLot = new AuctionLot(owner.getId(), symbol, minPrice, quantity);
    return auctionRepository.save(auctionLot);
  }

  public AuctionLot getById(int id) {
    var auctionLot = auctionRepository.findById(id);

    if (auctionLot.isEmpty()) {
      throw new ObjectNotFoundException("Auction with id " + id + " doesn't exist");
    }

    return auctionLot.get();
  }

  public List<AuctionLot> getAll() {
    return auctionRepository.findAll();
  }

  public NewBidResponse bid(int auctionId, int bidQuantity, double bidPrice, String username) {
    var auctionLot = getById(auctionId);

    if (auctionLot.getStatus().equals(String.valueOf(AuctionLot.Status.CLOSED))) {
      throw new BusinessException("Auction with ID " + auctionId + " is closed");
    }

    var bidder = userRepository.findByName(username);

    if (auctionLot.getOwnerId() == bidder.getId()) {
      throw new BusinessException("User cannot bid on owned auction");
    }

    if (bidQuantity < 0 || bidQuantity > auctionLot.getQuantity()) {
      throw new BusinessException(
          "bidding quantity must be be above 0 and/or not more than auction lot's quantity");
    }

    if (bidPrice < auctionLot.getMinPrice()) {
      throw new BusinessException(format("price needs to be above %s", auctionLot.getMinPrice()));
    }

    var bid = new Bid(auctionLot.getId(), bidder.getId(), bidQuantity, bidPrice);
    bidRepository.save(bid);

    return new NewBidResponse(
        auctionLot.getId(),
        auctionLot.getSymbol(),
        bidder.getUsername(),
        bidQuantity,
        bidPrice);
  }

  public List<Bid> getAllBids(int auctionId, Principal principal) {
    verifyOwnership(auctionId, principal);

    return bidRepository.getAuctionBids(auctionId);
  }

  public Optional<Bid> getBidByBidder(int auctionId, Principal principal) {
    var userId = userRepository.findByName(principal.getName()).getId();
    return bidRepository.getBidByBidderId(auctionId, userId);
  }

  public ClosingSummary close(int auctionId, Principal principal) {
    verifyOwnership(auctionId, principal);
    var auctionLot = getById(auctionId);

    if (auctionLot.getStatus().equals(String.valueOf(AuctionLot.Status.CLOSED))) {
      throw new BusinessException("Cannot close because already closed.");
    }

    saveAuctionBids(auctionLot);
    return getSummary(auctionId, principal);
  }

  private void saveAuctionBids(AuctionLot auctionLot) {
    var orderedBids = bidRepository.getBidsOrderedByPrice(auctionLot.getId());
    var availableQuantity = auctionLot.getQuantity();
    var revenue = BigDecimal.ZERO;

    for (Bid bid : orderedBids) {
      if (availableQuantity > 0) {
        var bidQuantity = min(availableQuantity, bid.getQuantity());
        bid.setState(Bid.State.WIN);
        bid.setWinQuantity(bidQuantity);

        availableQuantity -= bidQuantity;
        revenue = revenue.add(valueOf(bidQuantity).multiply(valueOf(bid.getPrice())));
      } else {
        bid.setState(Bid.State.LOST);
      }
    }

    bidRepository.saveAll(orderedBids);
    auctionLot.setTotalRevenue(revenue.doubleValue());
    auctionLot.setTotalSoldQuantity(auctionLot.getQuantity() - availableQuantity);
    auctionLot.setClosingTime(Instant.now());
    auctionLot.setStatus(AuctionLot.Status.CLOSED);
    auctionRepository.save(auctionLot);
  }

  public ClosingSummary getSummary(int auctionId, Principal principal) {
    verifyOwnership(auctionId, principal);
    var auctionLot = getById(auctionId);

    if (auctionLot.getStatus().equals(String.valueOf(AuctionLot.Status.OPENED))) {
      throw new BusinessException("AuctionLot must be closed to have a closing summary");
    }

    return new ClosingSummary(
        bidRepository.getOrderedWinningBids(auctionId),
        auctionLot.getTotalSoldQuantity(),
        auctionLot.getTotalRevenue(),
        auctionLot.getClosingTime());
  }

  private void verifyOwnership(int auctionId, Principal principal) {
    var userId = userRepository.findByName(principal.getName()).getId();
    var auctionLot = auctionRepository.validateAuctionOwner(auctionId, userId);

    if (auctionLot.isEmpty()) {
      throw new UnauthorizedException("The user is not the owner of auction lot.");
    }
  }

  public boolean isAuctionOwner(AuctionLot auctionLot, String name) {
    return auctionLot.getOwnerId() == userRepository.findByName(name).getId();
  }
}
