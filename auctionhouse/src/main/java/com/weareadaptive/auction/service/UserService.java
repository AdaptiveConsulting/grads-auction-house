package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ObjectNotFoundException;
import com.weareadaptive.auction.model.User;
import java.util.List;
import com.weareadaptive.auction.repository.UserRepository;
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
    if (userState.getUsernameIndex().containsKey(username)) {
      throw new BusinessException("username already exist");
    }

    var user = new User(userState.nextId(), username, password, firstName, lastName, organisation);
    userState.add(user);

    return user;

    /*
        var user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setOrganisation(organisation);
    userRepository.save(user);
    return user;
     */
  }

  public User getById(int id) {
    Optional<User> user = userState.getUsernameIndex().values()
        .stream().filter(u -> u.getId() == id).findFirst();

    if (user.isEmpty()) {
      throw new ObjectNotFoundException("User with " + id + " doesn't exist");
    }

    return user.get();
  }

  public User updateById(int id, String firstName, String lastName, String organisation) {
    User user = getById(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setOrganisation(organisation);

    return user;
  }

  public void blockById(int id) {
    User user = getById(id);
    user.block();
  }

  public void unblockById(int id) {
    User user = getById(id);
    user.unblock();
  }

  public List<User> getAll() {
    return userState.getUsernameIndex().values().stream().toList();
  }

  public Optional<User> validateUsernamePassword(String username, String password) {
    return userRepository.validateUsernamePassword(username, password);
  }
}
