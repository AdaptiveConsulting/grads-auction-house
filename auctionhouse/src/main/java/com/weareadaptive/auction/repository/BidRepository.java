package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.bid.Bid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BidRepository extends JpaRepository<Bid, Integer> {

  @Query("select b from Bid b where b.auctionId = :id ")
  List<Bid> getAllBidsByAuctionId(@Param("id") int id);
}
