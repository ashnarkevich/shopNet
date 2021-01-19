package com.gmail.petrikov05.app.web.controller.api.integration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_AMOUNT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_STATUS;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_TOTAL_PRICE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PHONE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = "SECURE_API_USER")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class OrderAPIControllerITTest {

    @Autowired
    private MockMvc mockMVC;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getOrders_returnOrders() throws Exception {
        List<OrderDTO> expectedOrderDTOS = getValidOrderDTOS();
        String expectedResult = objectMapper.writeValueAsString(expectedOrderDTOS);
        mockMVC.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void getOrderById_returnOrderDetails() throws Exception {
        OrderDetailsDTO expectedOrder = getValidOrderDetailsDTO();
        String expectedResult = objectMapper.writeValueAsString(expectedOrder);
        mockMVC.perform(get("/api/orders/" + VALID_ORDER_ID))
                .andExpect(content().json(expectedResult));
    }

    private OrderDetailsDTO getValidOrderDetailsDTO() {
        OrderDetailsDTO order = new OrderDetailsDTO();
        order.setId(VALID_ORDER_ID);
        order.setNumber(VALID_ORDER_NUMBER);
        order.setStatus(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        order.setDateCreate(VALID_ORDER_DATE);
        order.setItemName(VALID_ITEM_NAME);
        order.setItemAmount(VALID_ITEM_AMOUNT);
        order.setTotalPrice(VALID_TOTAL_PRICE);
        order.setCustomerId(2L);
        order.setCustomerPhone(VALID_USER_PHONE);
        return order;
    }

    private List<OrderDTO> getValidOrderDTOS() {
        return IntStream.of(1)
                .mapToObj(x -> getValidOrderDTO())
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