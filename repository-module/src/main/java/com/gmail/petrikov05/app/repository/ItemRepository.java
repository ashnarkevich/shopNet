package com.gmail.petrikov05.app.repository;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.Item;

public interface ItemRepository extends GenericRepository<Long, Item> {

    List<Item> getItemsByPage(int startPosition, int maxPosition);

    Item getItemsByNumber(String number);

}
