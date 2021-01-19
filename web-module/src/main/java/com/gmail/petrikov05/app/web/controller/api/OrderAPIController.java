package com.gmail.petrikov05.app.web.controller.api;

import java.util.List;
import java.util.Optional;

import com.gmail.petrikov05.app.service.OrderService;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.web.constant.MessageConstant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ORDER_NOT_FOUND;

@RestController
@RequestMapping("/api/orders")
public class OrderAPIController {

    private final OrderService orderService;

    public OrderAPIController(OrderService orderService) {this.orderService = orderService;}

    @GetMapping
    public List<OrderDTO> getOrders(
    ) {
        return orderService.getAllOrders();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getOrderById(
            @PathVariable Long id
    ) {
        OrderDetailsDTO order = orderService.getOrderById(id);
        if (order != null) {
            return order;
        } else {
            return MESSAGE_ORDER_NOT_FOUND;
        }
    }

}
