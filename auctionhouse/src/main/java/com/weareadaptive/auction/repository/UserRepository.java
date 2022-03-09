package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.User;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  @Query("select u from AuctionUser u where u.username=?1 and u.password=?2")
  Optional<User> validateUsernamePassword(String username, String password);

  @Transactional
  @Modifying
  @Query("update AuctionUser u set u.blocked=true where u.id=?1")
  int block(int id);

  @Transactional
  @Modifying
  @Query("update AuctionUser u set u.blocked=false where u.id=?1")
  int unblock(int id);
}
