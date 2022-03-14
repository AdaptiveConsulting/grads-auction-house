package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.AuctionLot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionLot, Integer> {
  @Query("select a from AuctionLot a where a.id = ?1 and a.ownerId = ?2")
  Optional<AuctionLot> validateAuctionOwner(int auctionId, int userId);

  @Query("select a.id, a.symbol, a.minPrice, a.quantity, a.status, a.createTime from AuctionLot a")
  AuctionLot getAuctionResponse(int id);
}
