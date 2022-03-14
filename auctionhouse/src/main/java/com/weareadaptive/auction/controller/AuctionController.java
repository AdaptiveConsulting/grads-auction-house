package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.AuctionBasicResponse;
import com.weareadaptive.auction.controller.dto.AuctionMapper;
import com.weareadaptive.auction.controller.dto.AuctionResponse;
import com.weareadaptive.auction.controller.dto.BidAuctionRequest;
import com.weareadaptive.auction.controller.dto.BidInfo;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.controller.dto.NewBidResponse;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.service.AuctionLotService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
  private final AuctionLotService auctionLotService;

  public AuctionController(AuctionLotService auctionLotService) {
    this.auctionLotService = auctionLotService;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  AuctionResponse create(@RequestBody @Valid CreateAuctionRequest createAuctionRequest,
                         Principal principal) {
    AuctionLot auctionLot = auctionLotService.create(
        principal.getName(),
        createAuctionRequest.symbol(),
        createAuctionRequest.minPrice(),
        createAuctionRequest.quantity());
    return new AuctionResponse(auctionLot, null);
  }

  @GetMapping("/{id}")
  AuctionBasicResponse getById(@PathVariable int id, Principal principal) {
    var auctionLot = auctionLotService.getById(id);

    return getAuctionResponse(auctionLot, principal);
  }

  @GetMapping()
  List<AuctionBasicResponse> getAll(Principal principal) {
    var auctionLots = auctionLotService.getAll();
    List<AuctionBasicResponse> responses = new ArrayList<>();
    auctionLots.forEach(auctionLot -> responses.add(getAuctionResponse(auctionLot, principal)));
    return responses;
  }

  private AuctionBasicResponse getAuctionResponse(AuctionLot auctionLot, Principal principal) {
    if (auctionLotService.isAuctionOwner(auctionLot, principal.getName())) {
      return AuctionMapper.map(
          auctionLot,
          auctionLotService.getAllBids(auctionLot.getId(), principal));
    } else {
      return AuctionMapper.mapBasicInfo(
          auctionLot,
          auctionLotService.getBidByBidder(auctionLot.getId(), principal).orElse(null));
    }
  }

  @PostMapping("/{id}/bid")
  NewBidResponse bidById(@PathVariable int id,
                         @RequestBody @Valid BidAuctionRequest bidAuctionRequest,
                         Principal principal) {
    return auctionLotService.bid(
        id,
        bidAuctionRequest.quantity(),
        bidAuctionRequest.price(),
        principal.getName());
  }

  @GetMapping("/{id}/all-bids")
  List<BidInfo> getBids(@PathVariable int id, Principal principal) {
    return AuctionMapper.mapAllBids(auctionLotService.getAllBids(id, principal));
  }

  @PostMapping("/{id}/close")
  ClosingSummary close(@PathVariable int id, Principal principal) {
    return auctionLotService.close(id, principal);
  }

  @GetMapping("/{id}/close-summary")
  ClosingSummary getSummary(@PathVariable int id, Principal principal) {
    return auctionLotService.getSummary(id, principal);
  }

}