package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.Bid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
  @Query("select b from AuctionBid b where b.auctionId = ?1")
  List<Bid> getAuctionBids(int auctionId);

  @Query("select b from AuctionBid b where b.auctionId = ?1 order by b.price desc")
  List<Bid> getBidsOrderedByPrice(int auctionId);

  @Query("select b from AuctionBid b where b.auctionId = ?1 and b.state = 'WIN' order by b.price desc")
  List<Bid> getOrderedWinningBids(int auctionId);

  @Query("select b from AuctionBid b where b.auctionId = ?1 and b.userId = ?2")
  Optional<Bid> getBidByBidderId(int auctionId, int userId);
}
