package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User create(String username, String password, String firstName, String lastName,
                     String organisation) {
    var user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setOrganisation(organisation);
    return userRepository.save(user);
  }

  public User getById(int id) {
    throw new UnsupportedOperationException();
  }

  public User updateById(int id, String firstName, String lastName, String organisation) {
    throw new UnsupportedOperationException();
  }

  public void blockById(int id) {
    throw new UnsupportedOperationException();
  }

  public void unblockById(int id) {
    throw new UnsupportedOperationException();
  }

  public List<User> getAll() {
    throw new UnsupportedOperationException();
  }

  public Optional<User> validateUsernamePassword(String username, String password) {
    return userRepository.validateUsernamePassword(username, password);
  }
}
