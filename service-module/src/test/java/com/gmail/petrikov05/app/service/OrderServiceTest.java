package com.gmail.petrikov05.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.repository.ItemRepository;
import com.gmail.petrikov05.app.repository.OrderRepository;
import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.repository.model.Order;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserInformation;
import com.gmail.petrikov05.app.repository.model.constant.OrderStatusEnum;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.exception.UserInformationException;
import com.gmail.petrikov05.app.service.impl.OrderServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.order.AddOrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum;
import com.gmail.petrikov05.app.service.model.order.UpdateOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import static com.gmail.petrikov05.app.repository.model.constant.UserRoleEnum.ADMINISTRATOR;
import static com.gmail.petrikov05.app.repository.model.constant.UserRoleEnum.CUSTOMER_USER;
import static com.gmail.petrikov05.app.repository.model.constant.UserRoleEnum.SALE_USER;
import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_ITEM_NOT_FOUND;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COUNT_OF_ENTITIES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_EMAIL;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_AMOUNT;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_PRICE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_OBJECT_BY_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ORDER_DATE_CREATE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ORDER_ID;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ORDER_NUMBER;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ORDER_STATUS;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PHONE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_START_POSITION;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_TOTAL_PRICE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    private void setup() {
        this.orderService = new OrderServiceImpl(orderRepository, itemRepository, userService);
    }

    /* get orders by page with pagination */
    @Test
    public void getOrdersByPageBySaleUser_returnValidOrders() throws AnonymousUserException {
        User returnedUser = getValidUser();
        returnedUser.setRole(SALE_USER);
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        List<Order> returnedOrders = getValidOrders();
        when(orderRepository.getCountOfEntities()).thenReturn(VALID_COUNT_OF_ENTITIES);
        when(orderRepository.getOrdersByPage(VALID_START_POSITION, VALID_OBJECT_BY_PAGE)).thenReturn(returnedOrders);
        PaginationWithEntitiesDTO<OrderDTO> actualResult = orderService.getOrdersByPage(VALID_PAGE);
        verify(orderRepository, times(1)).getOrdersByPage(VALID_START_POSITION, VALID_OBJECT_BY_PAGE);
        assertThat(actualResult.getEntities()).isNotNull();
        assertThat(actualResult.getEntities().get(0).getId()).isEqualTo(VALID_ORDER_ID);
        assertThat(actualResult.getEntities().get(0).getNumber()).isEqualTo(VALID_ORDER_NUMBER);
        assertThat(actualResult.getEntities().get(0).getStatus().name()).isEqualTo(VALID_ORDER_STATUS);
        assertThat(actualResult.getEntities().get(0).getDateCreate()).isEqualTo(VALID_ORDER_DATE_CREATE);
        assertThat(actualResult.getEntities().get(0).getItemName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getEntities().get(0).getItemAmount()).isEqualTo(VALID_ITEM_AMOUNT);
        assertThat(actualResult.getEntities().get(0).getTotalPrice()).isEqualTo(VALID_TOTAL_PRICE);
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidPages() throws AnonymousUserException {
        User returnedUser = getValidUser();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        when(orderRepository.getCountOfEntities()).thenReturn(VALID_COUNT_OF_ENTITIES);
        PaginationWithEntitiesDTO<OrderDTO> actualResult = orderService.getOrdersByPage(VALID_PAGE);
        verify(orderRepository, times(1)).getCountOfEntities();
        assertThat(actualResult.getPages()).isNotNull();
        assertThat(actualResult.getPages()).isEqualTo(VALID_PAGES);
    }

    @Test
    public void getOrdersByPage_returnEmptyCollection() throws AnonymousUserException {
        User returnedUser = getValidUser();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        when(orderRepository.getCountOfEntities()).thenReturn(0L);
        PaginationWithEntitiesDTO<OrderDTO> actualResult = orderService.getOrdersByPage(VALID_PAGE);
        assertThat(actualResult.getEntities()).isEmpty();
        assertThat(actualResult.getPages()).isZero();
    }

    /* get order by number */
    @Test
    public void getOrderByNumber_returnValidOrder() {
        Order returnedOrder = getValidOrder();
        when(orderRepository.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        OrderDetailsDTO actualResult = orderService.getOrderByNumber(VALID_ORDER_NUMBER);
        verify(orderRepository, times(1)).getOrderByNumber(anyString());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getNumber()).isEqualTo(VALID_ORDER_NUMBER);
        assertThat(actualResult.getStatus()).isEqualByComparingTo(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        assertThat(actualResult.getItemName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getItemAmount()).isEqualTo(VALID_ITEM_AMOUNT);
        assertThat(actualResult.getTotalPrice()).isEqualTo(VALID_TOTAL_PRICE);
        assertThat(actualResult.getCustomerId()).isEqualTo(VALID_USER_ID);
        assertThat(actualResult.getCustomerPhone()).isEqualTo(VALID_PHONE);
    }

    @Test
    public void getNonExistentOrderByNumber_returnNull() {
        when(orderRepository.getOrderByNumber(anyString())).thenReturn(null);
        OrderDetailsDTO actualResult = orderService.getOrderByNumber(VALID_ORDER_NUMBER);
        assertThat(actualResult).isNull();
    }

    /* update order / by number */
    @Test
    public void updateOrder_returnUpdatedOrder() {
        UpdateOrderDTO updateOrderDTO = getValidUpdateOrderDTO();
        Order returnedOrder = getValidOrder();
        when(orderRepository.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        OrderDetailsDTO actualResult = orderService.updateOrder(VALID_ORDER_NUMBER, updateOrderDTO);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getNumber()).isEqualTo(VALID_ORDER_NUMBER);
        assertThat(actualResult.getStatus()).isEqualByComparingTo(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        assertThat(actualResult.getItemName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getItemAmount()).isEqualTo(VALID_ITEM_AMOUNT);
        assertThat(actualResult.getTotalPrice()).isEqualTo(VALID_TOTAL_PRICE);
        assertThat(actualResult.getCustomerId()).isEqualTo(VALID_USER_ID);
        assertThat(actualResult.getCustomerPhone()).isEqualTo(VALID_PHONE);
    }

    @Test
    public void updateNonExistentOrder_returnNull() {
        UpdateOrderDTO updateOrderDTO = getValidUpdateOrderDTO();
        when(orderRepository.getOrderByNumber(anyString())).thenReturn(null);
        OrderDetailsDTO actualResult = orderService.updateOrder(anyString(), updateOrderDTO);
        assertThat(actualResult).isNull();
    }

    /* add order */
    @Test
    public void addOrder_returnAddedOrder() throws AnonymousUserException, ObjectDBException, UserInformationException {
        AddOrderDTO addOrderDTO = getValidAddOrderDTO();
        User returnedUser = getValidUser();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        Optional<Item> returnedItem = Optional.of(getValidItem());
        when(itemRepository.getItemByNumber(any())).thenReturn(returnedItem);
        Order returnedOrder = getValidOrder();
        when(orderRepository.add(any())).thenReturn(returnedOrder);
        OrderDetailsDTO actualResult = orderService.addOrder(addOrderDTO);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getNumber()).isEqualTo(VALID_ORDER_NUMBER);
        assertThat(actualResult.getStatus().name()).isEqualTo(VALID_ORDER_STATUS);
        assertThat(actualResult.getCustomerId()).isEqualTo(VALID_USER_ID);
        assertThat(actualResult.getCustomerPhone()).isEqualTo(VALID_PHONE);
        assertThat(actualResult.getItemName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getItemAmount()).isEqualTo(VALID_ITEM_AMOUNT);
        assertThat(actualResult.getTotalPrice()).isEqualByComparingTo(VALID_TOTAL_PRICE);
    }

    @Test
    public void addOrder_callLogic() throws AnonymousUserException, ObjectDBException, UserInformationException {
        Optional<Item> returnedItem = Optional.of(getValidItem());
        User returnedUser = getValidUser();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        when(itemRepository.getItemByNumber(any())).thenReturn(returnedItem);
        Order returnedOrder = getValidOrder();
        when(orderRepository.add(any())).thenReturn(returnedOrder);
        AddOrderDTO addOrderDTO = getValidAddOrderDTO();
        orderService.addOrder(addOrderDTO);
        verify(userService, times(1)).getCurrentUser();
        verify(itemRepository, times(1)).getItemByNumber(anyString());
        verify(orderRepository, times(1)).add(any());
    }

    @Test
    public void addOrderWithNonExistentItem_returnObjectDBException() throws AnonymousUserException {
        AddOrderDTO addOrderDTO = getValidAddOrderDTO();
        User returnedUser = getValidUser();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        when(itemRepository.getItemByNumber(anyString())).thenReturn(Optional.empty());
        assertThatExceptionOfType(ObjectDBException.class)
                .isThrownBy(() -> orderService.addOrder(addOrderDTO));
        assertThrows(
                ObjectDBException.class,
                () -> orderService.addOrder(addOrderDTO),
                MESSAGE_ITEM_NOT_FOUND);
    }

    @Test
    public void addOrderWithAnonymousUser_returnAnonymousUserException() throws AnonymousUserException {
        AddOrderDTO addOrderDTO = getValidAddOrderDTO();
        when(userService.getCurrentUser()).thenThrow(new AnonymousUserException());
        assertThatExceptionOfType(AnonymousUserException.class)
                .isThrownBy(() -> orderService.addOrder(addOrderDTO));
        assertThrows(AnonymousUserException.class, () -> orderService.addOrder(addOrderDTO));
    }

    @Test
    public void addOrderWithUserWithEmptyPhone_returnUserInformationException() throws AnonymousUserException {
        AddOrderDTO addOrderDTO = getValidAddOrderDTO();
        User returnedUser = getValidUser();
        UserInformation userInformation = new UserInformation();
        returnedUser.setUserInformation(userInformation);
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        assertThatExceptionOfType(UserInformationException.class)
                .isThrownBy(() -> orderService.addOrder(addOrderDTO));
        assertThrows(UserInformationException.class, () -> orderService.addOrder(addOrderDTO));
    }

    /* get orders by user role / by customer_user*/
    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getOrdersByCustomer_returnOrders() throws AnonymousUserException {
        User returnedUser = getValidUser();
        returnedUser.setRole(CUSTOMER_USER);
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        List<Order> returnedOrders = getValidOrders();
        when(orderRepository.getOrdersByPageByUser(anyInt(), anyInt(), any())).thenReturn(returnedOrders);
        PaginationWithEntitiesDTO<OrderDTO> actualResult = orderService.getOrdersByPage(VALID_PAGE);
        verify(orderRepository, times(1))
                .getOrdersByPageByUser(VALID_START_POSITION, VALID_OBJECT_BY_PAGE, returnedUser);
        assertThat(actualResult.getEntities()).isNotNull();
        assertThat(actualResult.getEntities().get(0).getId()).isEqualTo(VALID_ORDER_ID);
        assertThat(actualResult.getEntities().get(0).getNumber()).isEqualTo(VALID_ORDER_NUMBER);
        assertThat(actualResult.getEntities().get(0).getStatus().name()).isEqualTo(VALID_ORDER_STATUS);
        assertThat(actualResult.getEntities().get(0).getDateCreate()).isEqualTo(VALID_ORDER_DATE_CREATE);
        assertThat(actualResult.getEntities().get(0).getItemName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getEntities().get(0).getItemAmount()).isEqualTo(VALID_ITEM_AMOUNT);
        assertThat(actualResult.getEntities().get(0).getTotalPrice()).isEqualTo(VALID_TOTAL_PRICE);
    }

    /* get all orders */
    @Test
    public void getAllOrders_returnOrderDTOS() {
        List<Order> returnedOrders = getValidOrders();
        when(orderRepository.getAllObjects()).thenReturn(returnedOrders);
        List<OrderDTO> actualResult = orderService.getAllOrders();
        verify(orderRepository, times(1)).getAllObjects();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.get(0).getId()).isEqualTo(VALID_ORDER_ID);
        assertThat(actualResult.get(0).getNumber()).isEqualTo(VALID_ORDER_NUMBER);
        assertThat(actualResult.get(0).getDateCreate()).isEqualTo(VALID_ORDER_DATE_CREATE);
        assertThat(actualResult.get(0).getStatus().name()).isEqualTo(VALID_ORDER_STATUS);
        assertThat(actualResult.get(0).getItemName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.get(0).getItemAmount()).isEqualTo(VALID_ITEM_AMOUNT);
        assertThat(actualResult.get(0).getTotalPrice()).isEqualTo(VALID_TOTAL_PRICE);
    }

    /* get order by id */
    @Test
    public void getOrderById_returnOrderDTO() {
        Order returnedOrder = getValidOrder();
        when(orderRepository.getObjectByID(anyLong())).thenReturn(returnedOrder);
        OrderDetailsDTO actualResult = orderService.getOrderById(VALID_ORDER_ID);
        verify(orderRepository, times(1)).getObjectByID(anyLong());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getNumber()).isEqualTo(VALID_ORDER_NUMBER);
        assertThat(actualResult.getStatus().name()).isEqualTo(VALID_ORDER_STATUS);
        assertThat(actualResult.getDateCreate()).isEqualTo(VALID_ORDER_DATE_CREATE);
        assertThat(actualResult.getCustomerId()).isEqualTo(VALID_USER_ID);
        assertThat(actualResult.getCustomerPhone()).isEqualTo(VALID_PHONE);
        assertThat(actualResult.getItemName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getItemAmount()).isEqualTo(VALID_ITEM_AMOUNT);
        assertThat(actualResult.getTotalPrice()).isEqualByComparingTo(VALID_TOTAL_PRICE);
    }

    @Test
    public void getNonExistentOrderById_returnNull() throws AnonymousUserException {
        when(orderRepository.getObjectByID(anyLong())).thenReturn(null);
        OrderDetailsDTO actualResult = orderService.getOrderById(anyLong());
        assertThat(actualResult).isNull();
    }

    private AddOrderDTO getValidAddOrderDTO() {
        AddOrderDTO order = new AddOrderDTO();
        order.setItemNumber(VALID_ITEM_NUMBER);
        order.setItemAmount(VALID_ITEM_AMOUNT);
        return order;
    }

    private UpdateOrderDTO getValidUpdateOrderDTO() {
        UpdateOrderDTO order = new UpdateOrderDTO();
        order.setStatus(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        return order;
    }

    private List<Order> getValidOrders() {
        return IntStream.of(1)
                .mapToObj(x -> getValidOrder())
                .collect(Collectors.toList());
    }

    private Order getValidOrder() {
        Order order = new Order();
        order.setId(VALID_ORDER_ID);
        order.setNumber(VALID_ORDER_NUMBER);
        order.setStatus(OrderStatusEnum.valueOf(VALID_ORDER_STATUS));
        order.setDateCreate(VALID_ORDER_DATE_CREATE);
        order.setItem(getValidItem());
        order.setItemAmount(VALID_ITEM_AMOUNT);
        order.setTotalPrice(VALID_TOTAL_PRICE);
        order.setCustomer(getValidUser());
        return order;
    }

    private User getValidUser() {
        User user = new User();
        user.setId(VALID_USER_ID);
        user.setEmail(VALID_EMAIL);
        user.setRole(ADMINISTRATOR);
        UserInformation userInformation = getValidUserInformation();
        user.setUserInformation(userInformation);
        userInformation.setUser(user);
        return user;
    }

    private UserInformation getValidUserInformation() {
        UserInformation userInformation = new UserInformation();
        userInformation.setPhone(VALID_PHONE);
        return userInformation;
    }

    private Item getValidItem() {
        Item item = new Item();
        item.setName(VALID_ITEM_NAME);
        item.setPrice(VALID_ITEM_PRICE);
        return item;
    }

}