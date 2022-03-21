package com.weareadaptive.auction.model;

import com.weareadaptive.auction.exception.KeyDoesNotExistException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class State<T extends Entity> {
  public static final String ITEM_ALREADY_EXISTS = "Item already exists";
  public static final String ITEM_DOES_NOT_EXIST = "Item does not exist";
  private final Map<Integer, T> entities;
  private int currentId = 1;

  public State() {
    entities = new HashMap<>();
  }

  public int nextId() {
    return currentId++;
  }

  protected void onAdd(T model) {

  }

  public void add(T model) {
    if (entities.containsKey(model.getId())) {
      throw new BusinessException(ITEM_ALREADY_EXISTS);
    }
    onAdd(model);
    entities.put(model.getId(), model);
  }

  void setNextId(int id) {
    this.currentId = id;
  }

  public T get(int id) {
    if (!entities.containsKey(id)) {
      throw new KeyDoesNotExistException(ITEM_DOES_NOT_EXIST);
    }
    return entities.get(id);
  }

  public Stream<T> stream() {
    return entities.values().stream();
  }
}
