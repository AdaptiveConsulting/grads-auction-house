package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ObjectNotFoundException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public record UserService(UserRepository userRepository) {

  public User create(String username, String password, String firstName, String lastName,
                     String organisation) {
    var user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setOrganisation(organisation);
    try {
      return userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      var cause = e.getCause();

      if (cause instanceof ConstraintViolationException
          && ((ConstraintViolationException) cause).getConstraintName()
          .equals("auction_user_username_key")) {
        throw new BusinessException(String.format("%s already exist", username));
      }
      throw e;
    }
  }

  public User getById(int id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " doesn't exist"));
  }

  public User updateById(int id, String firstName, String lastName, String organisation) {
    var user = userRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " doesn't exist"));
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setOrganisation(organisation);
    return userRepository.save(user);
  }

  public void blockById(int id) {
    var count = userRepository.block(id);

    if (count == 0) {
      throw new ObjectNotFoundException(String.format("User with id %d doesn't exist", id));
    }
  }

  public void unblockById(int id) {
    var count = userRepository.unblock(id);

    if (count == 0) {
      throw new ObjectNotFoundException(String.format("User with id %d doesn't exist", id));
    }
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }

  public Optional<User> validateUsernamePassword(String username, String password) {
    return userRepository.validateUsernamePassword(username, password);
  }
}
