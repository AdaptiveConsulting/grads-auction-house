package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.AuctionMapper;
import com.weareadaptive.auction.controller.dto.AuctionResponse;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.service.AuctionLotService;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
        getOwnerName(),
        createAuctionRequest.symbol(),
        createAuctionRequest.minPrice(),
        createAuctionRequest.quantity());
    return AuctionMapper.map(auctionLot);
  }

  @ResponseBody
  public String currentUserNameSimple(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    return principal.getName();
  }

  private String getOwnerName() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
    /*if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    } else {
      return principal.toString();
    }*/
  }
}