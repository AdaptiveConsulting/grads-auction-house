package com.weareadaptive.auction.model;


import static org.apache.logging.log4j.util.Strings.isBlank;

public class User implements Entity {
  private final int id;
  private final String username;
  private final String password;
  private final boolean isAdmin;
  private String firstName;
  private String lastName;
  private String organisation;
  private String phone;
  private String email;
  private boolean blocked;

  public User(
      int id,
      String username,
      String password,
      String firstName,
      String lastName,
      String organisation) {
    this(id, username, password, firstName, lastName, organisation, false);
  }

  //test

  //another test

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

  public String getOrganisation() {
    return organisation;
  }

  public void setOrganisation(String organisation) {
    this.organisation = organisation;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public String getPhone() {
    return phone;
  }

  public String getEmail() {
    return email;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public void block() {
    blocked = true;
  }

  public void unblock() {
    blocked = false;
  }
}
