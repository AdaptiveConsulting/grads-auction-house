package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.AuctionMapper;
import com.weareadaptive.auction.controller.dto.AuctionResponse;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.service.AuctionLotService;
import java.util.ArrayList;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
  AuctionResponse create(@RequestBody @Valid CreateAuctionRequest createAuctionRequest) {
    AuctionLot auctionLot = auctionLotService.create(
        getCurrentUsername(),
        createAuctionRequest.symbol(),
        createAuctionRequest.minPrice(),
        createAuctionRequest.quantity());
    return AuctionMapper.map(auctionLot);
  }

  @GetMapping("/{id}")
  AuctionResponse getById(@PathVariable int id) {
    return AuctionMapper.map(auctionLotService.getById(id, getCurrentUsername()));
  }

  @GetMapping()
  ArrayList<AuctionResponse> getAll() {
    return AuctionMapper.mapAll(auctionLotService.getAll());
  }

  private String getCurrentUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}