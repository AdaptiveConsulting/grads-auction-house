package com.weareadaptive.auction.controller;

import static com.weareadaptive.auction.dto.Mapper.map;

import com.weareadaptive.auction.dto.Mapper;
import com.weareadaptive.auction.dto.request.CreateAuctionRequest;
import com.weareadaptive.auction.dto.request.CreateBidRequest;
import com.weareadaptive.auction.dto.response.AuctionResponse;
import com.weareadaptive.auction.dto.response.BidResponse;
import com.weareadaptive.auction.model.auction.ClosingSummary;
import com.weareadaptive.auction.service.AuctionService;

import java.security.Principal;
import java.util.stream.Stream;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auctions")
@PreAuthorize("hasRole('ROLE_USER')")
public class AuctionController {
  private final AuctionService auctionService;

  public AuctionController(AuctionService auctionService) {
    this.auctionService = auctionService;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public AuctionResponse createAuction(
      @RequestBody @Valid CreateAuctionRequest createAuctionRequest,
      Principal principal) {

    return map(
        auctionService.create(
            principal,
            createAuctionRequest
        ));
  }

  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public AuctionResponse getAuction(@PathVariable @Valid int id) {

    return map(auctionService.getAuction(id));
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Stream<AuctionResponse> getAllAuctions() {
    return auctionService.getAllAuctions().map(Mapper::map);
  }


  @PostMapping("{id}/bid")
  @ResponseStatus(HttpStatus.CREATED)
  public BidResponse bidAuction(@PathVariable @Valid int id,
                                @RequestBody @Valid CreateBidRequest createBidRequest,
                                Principal principal) {
    return map(
        auctionService.bid(
            principal,
            id,
            createBidRequest
        ));
  }

  @GetMapping("{id}/bids")
  @ResponseStatus(HttpStatus.OK)
  public Stream<BidResponse> getAllBids(@PathVariable @Valid int id,
                                        Principal principal) {
    return auctionService
        .getAllBids(id, principal)
        .map(Mapper::map);
  }

  @PostMapping("{id}/close")
  @ResponseStatus(HttpStatus.OK)
  public ClosingSummary closeAuction(@PathVariable @Valid int id,
                                     Principal principal) {

    return auctionService.closeAuction(id, principal);
  }


  @GetMapping("{id}/summary")
  @ResponseStatus(HttpStatus.OK)
  public String getAuctionSummary(@PathVariable @Valid int id) {

    return auctionService.getAuctionSummary(id);
  }


}
