package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.auctions.AuctionLotResponse;
import com.weareadaptive.auction.controller.dto.auctions.ClosingSummaryResponse;
import com.weareadaptive.auction.controller.dto.auctions.CreateAuctionLotRequest;
import com.weareadaptive.auction.controller.dto.bids.BidResponse;
import com.weareadaptive.auction.controller.dto.bids.CreateBidRequest;
import com.weareadaptive.auction.controller.mapper.AuctionLotMapper;
import com.weareadaptive.auction.controller.mapper.BidMapper;
import com.weareadaptive.auction.controller.mapper.ClosingSummaryMapper;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.service.AuctionLotService;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class AuctionLotController {
  private final AuctionLotService auctionLotService;

  public AuctionLotController(AuctionLotService auctionLotService) {
    this.auctionLotService = auctionLotService;
  }

  @PostMapping("/auctions")
  @ResponseStatus(HttpStatus.CREATED)
  public AuctionLotResponse createAuctionLot(
      Principal principal,
      @RequestBody @Valid CreateAuctionLotRequest createAuctionLotRequest) {
    AuctionLot auctionLot = auctionLotService.createAuctionLot(
        principal.getName(),
        createAuctionLotRequest.symbol(),
        createAuctionLotRequest.minPrice(),
        createAuctionLotRequest.quantity());
    return AuctionLotMapper.map(auctionLot);
  }

  @GetMapping("/auctions/{id}")
  public AuctionLotResponse getAuctionLot(@PathVariable int id) {
    AuctionLot auctionLot = auctionLotService.getAuctionLot(id);
    return AuctionLotMapper.map(auctionLot);
  }

  @GetMapping("/auctions")
  public List<AuctionLotResponse> getAllAuctionLots() {
    return auctionLotService.getAllAuctionLots()
      .map(AuctionLotMapper::map)
      .toList();
  }

  @PostMapping("/auctions/{id}/bids")
  public BidResponse createBid(
      Principal principal,
      @RequestBody @Valid CreateBidRequest createBidRequest,
      @PathVariable int id) {
    Bid bid = auctionLotService.createBid(
        principal.getName(),
        id,
        createBidRequest.price(),
        createBidRequest.quantity()
    );
    return BidMapper.map(bid);
  }

  @GetMapping("/auctions/{id}/bids")
  public List<BidResponse> getBids(
      Principal principal,
      @PathVariable int id) {
    return auctionLotService.getBids(principal.getName(), id)
      .stream()
      .map(BidMapper::map)
      .toList();
  }

  @PutMapping("/auctions/{id}/close")
  public ClosingSummaryResponse close(
      Principal principal,
      @PathVariable int id) {
    ClosingSummary closingSummary = auctionLotService.close(principal.getName(), id);
    return ClosingSummaryMapper.map(closingSummary);
  }

  @GetMapping("/auctions/{id}/closingSummary")
  public ClosingSummaryResponse getClosingSummary(@PathVariable int id) {
    ClosingSummary closingSummary = auctionLotService.getClosingSummary(id);
    return ClosingSummaryMapper.map(closingSummary);
  }
}
