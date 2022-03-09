package com.weareadaptive.auction.model;


import static org.apache.logging.log4j.util.Strings.isBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "AuctionUser")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String username;
  private String password;
  private boolean isAdmin;
  private String firstName;
  private String lastName;
  private String organisation;
  private boolean blocked;

  public User() {
  }

  public User(
      int id,
      String username,
      String password,
      String firstName,
      String lastName,
      String organisation) {
    this(id, username, password, firstName, lastName, organisation, false);
  }

  public User(
      int id,
      String username,
      String password,
      String firstName,
      String lastName,
      String organisation,
      boolean isAdmin) {
    if (isBlank(username)) {
      throw new BusinessException("username cannot be null or empty");
    }
    if (isBlank(password)) {
      throw new BusinessException("password cannot be null or empty");
    }
    if (isBlank(firstName)) {
      throw new BusinessException("firstName cannot be null or empty");
    }
    if (isBlank(lastName)) {
      throw new BusinessException("lastName cannot be null or empty");
    }
    if (isBlank(organisation)) {
      throw new BusinessException("organisation cannot be null or empty");
    }
    //TODO: Add regex for email
    this.id = id;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.organisation = organisation;
    this.isAdmin = isAdmin;
  }

  @Override
  public String toString() {
    return "User{"
        + "username='" + username + '\''
        + '}';
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean validatePassword(String password) {
    return this.password.equals(password);
  }

  public String getFirstName() {
    return firstName;
  }

  //TODO: Add validation
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getOrganisation() {
    return organisation;
  }

  public void setOrganisation(String organisation) {
    this.organisation = organisation;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public void block() {
    blocked = true;
  }

  public void unblock() {
    blocked = false;
  }
}
