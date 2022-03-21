package com.weareadaptive.auction.service;

import static java.util.Collections.reverseOrder;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;

import com.weareadaptive.auction.dto.request.CreateAuctionRequest;
import com.weareadaptive.auction.dto.request.CreateBidRequest;
import com.weareadaptive.auction.exception.http.BadRequestException;
import com.weareadaptive.auction.exception.http.NotAllowedException;
import com.weareadaptive.auction.exception.http.NotFoundException;
import com.weareadaptive.auction.model.auction.Auction;
import com.weareadaptive.auction.model.auction.ClosingSummary;
import com.weareadaptive.auction.model.bid.Bid;
import com.weareadaptive.auction.model.bid.WinningBid;
import com.weareadaptive.auction.model.user.AuctionUser;
import com.weareadaptive.auction.repository.AuctionRepository;
import com.weareadaptive.auction.repository.BidRepository;
import com.weareadaptive.auction.repository.UserRepository;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuctionRepository auctionRepository;

  @Autowired
  private BidRepository bidRepository;

  public Auction create(Principal principal, CreateAuctionRequest createAuction) {
    auctionExists(createAuction.symbol());

    var username = principal.getName();

    Auction auction = Auction.builder()
        .minPrice(createAuction.minPrice())
        .quantity(createAuction.quantity())
        .owner(username)
        .symbol(createAuction.symbol())
        .status(Auction.Status.OPENED.toString())
        .build();

    auctionRepository.save(auction);

    return auction;
  }

  public Auction getAuction(int id) {
    var auction = auctionRepository.getById(id);

    if (auction.isEmpty()) {
      throw new NotFoundException("Auction not found!");
    }

    return auction.get();
  }

  public Auction getAuction(String symbol) {
    var auction = auctionRepository.getBySymbol(symbol);

    if (auction.isEmpty()) {
      throw new NotFoundException("Auction not found!");
    }

    return auction.get();
  }

  public Stream<Auction> getAllAuctions() {
    return auctionRepository.getAllAuctions().stream();
  }

  public Bid bid(Principal principal, int auctionId, CreateBidRequest createBidRequest) {
    var auction = getAuction(auctionId);
    var user = principal.getName();

    if (isOwner(auction, principal)) {
      throw new NotAllowedException("Owner cannot bid on his/her own auction!");
    }

    if (createBidRequest.price() < auction.getMinPrice()) {
      throw new BadRequestException("Invalid! Bid price must be >= auction price");
    }

    var bid = Bid
        .builder()
        .price(createBidRequest.price())
        .quantity(createBidRequest.quantity())
        .username(user)
        .auctionId(auctionId)
        .state("PENDING")
        .build();

    bidRepository.save(bid);
    return bid;
  }

  public Stream<Bid> getAllBids(int auctionId, Principal principal) {
    var auction = getAuction(auctionId);

    if (!isOwner(auction, principal)) {
      throw new NotAllowedException("Only owner is allowed to get all bids");
    }

    return bidRepository.getAllBidsByAuctionId(auctionId).stream();
  }

  public ClosingSummary closeAuction(int auctionId, Principal principal) {
    var auction = getAuction(auctionId);
    isClosed(auction);

    if (!isOwner(auction, principal)) {
      throw new NotAllowedException("Only the owner can close his/her auction!");
    }
    return close(auction);
  }

  public String getAuctionSummary(int id) {
    var auction = getAuction(id);

    if (!auction.isClosed()) {
      throw new NotAllowedException("Error! cannot get a summary from an open auction!");
    }

    return auction.getClosingSummary();
  }

  /*====================Private Methods====================*/

  private void isClosed(Auction auction) {
    if (auction.isClosed()) {
      throw new NotAllowedException("Cannot perform this action! Auction is closed!");
    }
  }

  private AuctionUser getUser(Principal principal) {
    var username = principal.getName();

    var user = userRepository.getByUsername(username);

    if (user.isEmpty()) {
      throw new NotFoundException(String.format("User %s not found!", username));
    }

    return user.get();
  }

  private boolean isOwner(Auction auction, Principal principal) {
    return (getUser(principal).getUsername().equals(auction.getOwner()));
  }

  private ClosingSummary close(Auction auction) {
    if (auction.isClosed()) {
      throw new NotAllowedException("Cannot close because already closed.");
    }

    List<Bid> bids = bidRepository.getAllBidsByAuctionId(auction.getId());
    auction.close();
    var orderedBids = bids
        .stream()
        .sorted(reverseOrder(comparing(Bid::getPrice))
            .thenComparing(reverseOrder(comparingInt(Bid::getQuantity)))).toList();

    var availableQuantity = auction.getQuantity();
    var revenue = BigDecimal.ZERO;
    var winningBids = new ArrayList<WinningBid>();

    for (Bid bid : orderedBids) {
      if (availableQuantity > 0) {
        var bidQuantity = Math.min(availableQuantity, bid.getQuantity());

        winningBids.add(new WinningBid(bidQuantity, bid));
        win(bid, bidQuantity);
        availableQuantity -= bidQuantity;
        revenue = revenue.add(BigDecimal.valueOf(bidQuantity * bid.getPrice()));
      } else {
        lost(bid);
      }
      bidRepository.save(bid);
    }

    auction.setCloseTimestamp(Instant.now());

    var summary = new ClosingSummary(
        unmodifiableList(winningBids),
        auction.getQuantity() - availableQuantity,
        revenue,
        auction.getCloseTimestamp());

    auction.setClosingSummary(summary.toString());
    auctionRepository.saveAndFlush(auction);
    return summary;
  }

  public void lost(Bid bid) {

    if (bid.isPending()) {
      throw new NotAllowedException("Must be a pending bid");
    }

    bid.setLost();
  }

  public void win(Bid bid, int winQuantity) {
    if (bid.isPending()) {
      throw new NotAllowedException("Must be a pending bid");
    }

    if (bid.getQuantity() < winQuantity) {
      throw new NotAllowedException("winQuantity must be lower or equal to to the bid quantity");
    }

    bid.setWin();
    bid.setWinQuantity(winQuantity);
  }

  private void auctionExists(String symbol) {

    if (auctionRepository.existsBySymbol(symbol)) {
      throw new BadRequestException("Auction already exists!");
    }
  }

}
