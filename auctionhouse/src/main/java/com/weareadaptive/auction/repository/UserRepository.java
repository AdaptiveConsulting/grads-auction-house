package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  @Query("select u from AuctionUser u where u.username=?1 and u.password=?2")
  public Optional<User> validateUsernamePassword(String username, String password);

  //List<User> findByLastName(String lastName);
}
