package com.gmail.petrikov05.app.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.petrikov05.app.repository.ItemRepository;
import com.gmail.petrikov05.app.repository.OrderRepository;
import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.repository.model.Order;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserInformation;
import com.gmail.petrikov05.app.repository.model.constant.OrderStatusEnum;
import com.gmail.petrikov05.app.repository.model.constant.UserRoleEnum;
import com.gmail.petrikov05.app.service.OrderService;
import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.exception.UserInformationException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.order.AddOrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.UpdateOrderDTO;
import com.gmail.petrikov05.app.service.util.PageUtil;
import com.gmail.petrikov05.app.service.util.converter.OrderConverter;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.repository.model.constant.OrderStatusEnum.NEW;
import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_ITEM_NOT_FOUND;
import static com.gmail.petrikov05.app.service.constant.PageConstant.COUNT_OF_ORDER_BY_PAGE;
import static com.gmail.petrikov05.app.service.util.PageUtil.getStartPosition;
import static com.gmail.petrikov05.app.service.util.converter.OrderConverter.convertAddDTOToObject;
import static com.gmail.petrikov05.app.service.util.converter.OrderConverter.convertObjectToWithDetailsDTO;

@Service
public class OrderServiceImpl implements OrderService {

    private static int countOfOrdersPerYear = 1;
    private static int year = 2021;

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ItemRepository itemRepository,
            UserService userService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public PaginationWithEntitiesDTO<OrderDTO> getOrdersByPage(int page) throws AnonymousUserException {
        int startPosition = getStartPosition(page, COUNT_OF_ORDER_BY_PAGE);
        List<Order> orders = getOrdersByPageByUser(startPosition);
        List<OrderDTO> orderDTOS = orders.stream()
                .map(OrderConverter::convertObjectToDTO)
                .collect(Collectors.toList());
        int pages = getPages();
        return new PaginationWithEntitiesDTO<>(orderDTOS, pages);
    }

    @Override
    @Transactional
    public OrderDetailsDTO getOrderByNumber(String number) {
        Order order = orderRepository.getOrderByNumber(number);
        if (order == null) {
            return null;
        }
        return convertObjectToWithDetailsDTO(order);
    }

    @Override
    @Transactional
    public OrderDetailsDTO updateOrder(String number, UpdateOrderDTO updateOrderDTO) {
        Order order = orderRepository.getOrderByNumber(number);
        if (order == null) {
            return null;
        }
        if (!order.getStatus().name().equals(updateOrderDTO.getStatus().name())) {
            String status = updateOrderDTO.getStatus().name();
            order.setStatus(OrderStatusEnum.valueOf(status));
        }
        return convertObjectToWithDetailsDTO(order);
    }

    @Override
    @Transactional
    public OrderDetailsDTO addOrder(AddOrderDTO orderDTO) throws AnonymousUserException, ObjectDBException, UserInformationException {
        Order order = convertAddDTOToObject(orderDTO);
        User user = getCurrentUser();
        order.setCustomer(user);
        Item item = getItem(orderDTO.getItemNumber());
        order.setItem(item);
        BigDecimal totalPrice = item.getPrice()
                .multiply(BigDecimal.valueOf(orderDTO.getItemAmount()));
        order.setTotalPrice(totalPrice);
        String number = getOrderNumber();
        order.setNumber(number);
        order.setStatus(NEW);
        order = orderRepository.add(order);
        return convertObjectToWithDetailsDTO(order);
    }

    @Override
    @Transactional
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.getAllObjects();
        return orders.stream()
                .map(OrderConverter::convertObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDetailsDTO getOrderById(Long id) {
        Order order = orderRepository.getObjectByID(id);
        if (order != null) {
            return convertObjectToWithDetailsDTO(order);
        } else {
            return null;
        }
    }

    private List<Order> getOrdersByPageByUser(int startPosition) throws AnonymousUserException {
        User user = userService.getCurrentUser();
        UserRoleEnum userRole = user.getRole();
        switch (userRole) {
            case CUSTOMER_USER: {
                return orderRepository.getOrdersByPageByUser(startPosition, COUNT_OF_ORDER_BY_PAGE, user);
            }
            case SALE_USER: {
                return orderRepository.getOrdersByPage(startPosition, COUNT_OF_ORDER_BY_PAGE);
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    private User getCurrentUser() throws AnonymousUserException, UserInformationException {
        User user = userService.getCurrentUser();
        UserInformation userInformation = user.getUserInformation();
        if (userInformation.getPhone() == null) {
            throw new UserInformationException();
        }
        return user;
    }

    private String getOrderNumber() {
        int currentYear = LocalDate.now().getYear();
        if (currentYear != year) {
            countOfOrdersPerYear = 0;
            year = currentYear;
        }
        return countOfOrdersPerYear++ + "-" + currentYear;
    }

    private Item getItem(String number) throws ObjectDBException {
        Optional<Item> item = itemRepository.getItemByNumber(number);
        if (!item.isPresent()) {
            throw new ObjectDBException(MESSAGE_ITEM_NOT_FOUND);
        }
        return item.get();
    }

    private int getPages() {
        Long countOfEntities = orderRepository.getCountOfEntities();
        return PageUtil.getCountOfPage(countOfEntities, COUNT_OF_ORDER_BY_PAGE);
    }

}
