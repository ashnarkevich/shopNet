package com.gmail.petrikov05.app.service.util.converter;

import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.repository.model.Order;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.constant.OrderStatusEnum;
import com.gmail.petrikov05.app.service.model.order.AddOrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum;

public class OrderConverter {

    public static OrderDTO convertObjectToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setNumber(order.getNumber());
        OrderStatusEnum status = order.getStatus();
        orderDTO.setStatus(OrderStatusDTOEnum.valueOf(status.name()));
        orderDTO.setDateCreate(order.getDateCreate());
        Item item = order.getItem();
        orderDTO.setItemName(item.getName());
        orderDTO.setItemAmount(order.getItemAmount());
        orderDTO.setTotalPrice(order.getTotalPrice());
        return orderDTO;
    }

    public static OrderDetailsDTO convertObjectToWithDetailsDTO(Order order) {
        OrderDetailsDTO orderDTO = new OrderDetailsDTO();
        orderDTO.setId(order.getId());
        orderDTO.setNumber(order.getNumber());
        orderDTO.setStatus(OrderStatusDTOEnum.valueOf(order.getStatus().name()));
        orderDTO.setDateCreate(order.getDateCreate());
        orderDTO.setItemName(order.getItem().getName());
        orderDTO.setItemAmount(order.getItemAmount());
        orderDTO.setTotalPrice(order.getTotalPrice());

        User customer = order.getCustomer();
        orderDTO.setCustomerId(customer.getId());
        orderDTO.setCustomerPhone(customer.getUserInformation().getPhone());

        return orderDTO;
    }

    public static Order convertAddDTOToObject(AddOrderDTO orderDTO) {
        Order order = new Order();
        order.setItemAmount(orderDTO.getItemAmount());
        return order;
    }

}
