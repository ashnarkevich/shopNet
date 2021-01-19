package com.gmail.petrikov05.app.web.controller.api;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.OrderService;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.constant.MessageConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ORDER_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_AMOUNT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_STATUS;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_TOTAL_PRICE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PHONE;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderAPIController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "SECURE_API_USER")
class OrderAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    /* get orders */
    @Test
    public void getOrders_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getOrders_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/api/orders")
        ).andExpect(status().isOk());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void getOrders_returnOrders() throws Exception {
        List<OrderDTO> returnedOrderDTOS = getValidOrderDTOS();
        String expectedResult = objectMapper.writeValueAsString(returnedOrderDTOS);
        when(orderService.getAllOrders()).thenReturn(returnedOrderDTOS);
        mockMvc.perform(get("/api/orders")
        ).andExpect(content().json(expectedResult));
    }

    /* get order by id */
    @Test
    public void getOrderById_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(status().isOk());
    }

    @Test
    public void getOrderById_returnStatusBadRequest() throws Exception {
        mockMvc.perform(get("/api/orders/a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getOrderById_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(status().isOk());
        verify(orderService, times(1)).getOrderById(anyLong());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidId() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.id", is(VALID_ORDER_ID)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidNumber() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.number", is(VALID_ORDER_NUMBER)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidStatus() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.status", is(VALID_ORDER_STATUS)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidDateCreate() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.dateCreate", is(VALID_ORDER_DATE)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidCustomerId() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.customerId", is(VALID_ID)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidCustomerPhone() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.customerPhone", is(VALID_USER_PHONE)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidItemName() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.itemName", is(VALID_ITEM_NAME)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidItemAmount() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.itemAmount", is(VALID_ITEM_AMOUNT)).exists());
    }

    @Test
    public void getOrderById_returnOrderDTOWithValidTotalPrice() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(returnedOrder);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$.totalPrice", is(VALID_TOTAL_PRICE)).exists());
    }

    @Test
    public void getNonExistentOrderById_returnMessageWithStatus() throws Exception {
        when(orderService.getOrderById(anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/orders/" + VALID_ORDER_ID)
        ).andExpect(jsonPath("$", is(MESSAGE_ORDER_NOT_FOUND)).exists());
    }

    private OrderDetailsDTO getValidOrderDetailsDTO() {
        OrderDetailsDTO order = new OrderDetailsDTO();
        order.setId(VALID_ORDER_ID);
        order.setNumber(VALID_ORDER_NUMBER);
        order.setStatus(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        order.setDateCreate(VALID_ORDER_DATE);
        order.setCustomerId(VALID_ID);
        order.setCustomerPhone(VALID_USER_PHONE);
        order.setItemName(VALID_ITEM_NAME);
        order.setItemAmount(VALID_ITEM_AMOUNT);
        order.setTotalPrice(VALID_TOTAL_PRICE);
        return order;
    }

    private List<OrderDTO> getValidOrderDTOS() {
        return IntStream.of(1)
                .boxed()
                .map(x -> getValidOrderDTO())
                .collect(Collectors.toList());
    }

    private OrderDTO getValidOrderDTO() {
        OrderDTO order = new OrderDTO();
        order.setId(VALID_ORDER_ID);
        order.setNumber(VALID_ORDER_NUMBER);
        order.setDateCreate(VALID_ORDER_DATE);
        order.setStatus(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        order.setItemName(VALID_ITEM_NAME);
        order.setItemAmount(VALID_ITEM_AMOUNT);
        order.setTotalPrice(VALID_TOTAL_PRICE);
        return order;
    }

}