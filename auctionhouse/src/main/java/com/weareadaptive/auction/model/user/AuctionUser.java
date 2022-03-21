package com.weareadaptive.auction.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "auctionuser")
public class AuctionUser {

  private String username;

  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  private String firstName;

  private String lastName;

  private String password;

  private String organization;

  private boolean isBlocked;

  private boolean isAdmin;

  public boolean isBlocked() {
    return isBlocked;
  }

  public void block() {
    isBlocked = true;
  }

  public void unblock() {
    isBlocked = true;
  }

  public boolean isAdmin() {
    return isAdmin;
  }


}