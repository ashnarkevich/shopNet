package com.gmail.petrikov05.app.repository;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.Order;
import com.gmail.petrikov05.app.repository.model.User;

public interface OrderRepository extends GenericRepository<Long, Order> {

    List<Order> getOrdersByPage(int startPosition, int maxResult);

    Order getOrderByNumber(String number);

    List<Order> getOrdersByPageByUser(int startPosition, int maxResult, User user);

}
