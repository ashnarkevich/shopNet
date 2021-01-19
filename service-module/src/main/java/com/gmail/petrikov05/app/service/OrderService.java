package com.gmail.petrikov05.app.service;

import java.util.List;

import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.exception.UserInformationException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.order.AddOrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.UpdateOrderDTO;

public interface OrderService {

    PaginationWithEntitiesDTO<OrderDTO> getOrdersByPage(int page) throws AnonymousUserException;

    OrderDetailsDTO getOrderByNumber(String number);

    OrderDetailsDTO updateOrder(String number, UpdateOrderDTO updateOrderDTO);

    OrderDetailsDTO addOrder(AddOrderDTO orderDTO) throws AnonymousUserException, ObjectDBException, UserInformationException;

    List<OrderDTO> getAllOrders();

    OrderDetailsDTO getOrderById(Long id);

}
