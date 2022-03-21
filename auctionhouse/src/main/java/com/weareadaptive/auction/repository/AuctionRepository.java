package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.auction.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
  boolean existsBySymbol(String symbol);

  @Query("select a from auction a where a.symbol = :symbol")
  Optional<Auction> getBySymbol(@Param("symbol") String symbol);

  @Query("select a from auction a where a.id = :id ")
  Optional<Auction> getById(@Param("id") int id);

  @Query("select a from auction a")
  List<Auction> getAllAuctions();




}
