package com.gmail.petrikov05.app.web.controller.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.service.OrderService;
import com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage;
import com.gmail.petrikov05.app.service.constant.validation.ValidationMessages;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.exception.UserInformationException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.order.AddOrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum;
import com.gmail.petrikov05.app.service.model.order.UpdateOrderDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.controller.OrderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_ITEM_NOT_FOUND;
import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_USER_WITHOUT_INFORMATION;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_ITEM_AMOUNT_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_ORDER_STATUS_NOT_NULL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ACCESS_CLOSE;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ORDER_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ORDER_UPDATED;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_AMOUNT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_STATUS;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_TOTAL_PRICE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = OrderController.class)
@Import(TestConfig.class)
@WithMockUser(roles = {"SALE_USER", "CUSTOMER_USER"})
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Test
    public void getOrdersByPage_returnOk() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrdersWithPagination = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrdersWithPagination);
        mockMvc.perform(get("/orders").contentType(MediaType.TEXT_HTML_VALUE)
        ).andExpect(status().isOk())
                .andExpect(view().name("orders"));
    }

    @Test
    public void getOrdersByPage_returnBadRequest() throws Exception {
        mockMvc.perform(get("/orders").param("page", "q")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getOrdersByPage_callBusinessLogic() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrdersWithPagination = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrdersWithPagination);
        mockMvc.perform(get("/orders")).andExpect(status().isOk());
        verify(orderService, times(1)).getOrdersByPage(anyInt());
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidPage() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrdersWithPagination = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrdersWithPagination);
        mockMvc.perform(get("/orders").param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk())
                .andExpect(model().attribute("page", VALID_PAGE));
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidPages() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        mockMvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(model().attribute("pages", VALID_PAGES));
    }

    @Test
    public void getOrdersByPage_returnOrders() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        mockMvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidId() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidNumber() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ORDER_NUMBER);
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidStatus() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ORDER_STATUS);
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidDate() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ORDER_DATE));
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidItemName() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ITEM_NAME);
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidItemAmount() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
    }

    @Test
    public void getOrdersByPage_returnOrdersWithValidTotalPrice() throws Exception, AnonymousUserException {
        PaginationWithEntitiesDTO<OrderDTO> returnedOrders = getValidOrdersByPage();
        when(orderService.getOrdersByPage(anyInt())).thenReturn(returnedOrders);
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_TOTAL_PRICE));
    }

    @Test
    public void getOrdersByPageWithAnonymousUser_redirectIndexWithError() throws Exception, AnonymousUserException {
        when(orderService.getOrdersByPage(anyInt())).thenThrow(new AnonymousUserException());
        mockMvc.perform(get("/orders")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", MESSAGE_ACCESS_CLOSE));
    }

    /* get order by number */
    @Test
    public void getOrderByNumber_returnStatusOk() throws Exception {
        mockMvc.perform(get("/orders/20210107-25")).andExpect(status().isOk());
    }

    @Test
    public void getOrderByNumber_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/orders/20210107-25")).andExpect(status().isOk());
        verify(orderService, times(1)).getOrderByNumber(anyString());
    }

    @Test
    public void getOrderByNumber_returnOrder() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        mockMvc.perform(get("/orders/20210107-25"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    public void getOrderByNumber_returnOrderWithValidNumber() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ORDER_NUMBER);
    }

    @Test
    public void getOrderByNumber_returnOrderWithValidStatus() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ORDER_STATUS);
    }

    @Test
    public void getOrderByNumber_returnOrderWithValidItemName() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ITEM_NAME);
    }

    @Test
    public void getOrderByNumber_returnOrderWithValidUserId() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_USER_ID));
    }

    @Test
    public void getOrderByNumber_returnOrderWithValidUserPhone() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_PHONE);
    }

    @Test
    public void getOrderByNumber_returnOrderWithValidItemAmount() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
    }

    @Test
    public void getOrderByNumber_returnOrderWithValidTotalPrice() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_TOTAL_PRICE));
    }

    @Test
    public void getNonExistentOrder_returnMessage() throws Exception {
        when(orderService.getOrderByNumber(anyString())).thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(get("/orders/20210107-25")).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_ORDER_NOT_FOUND);
    }

    @Test
    public void getOrderByNumber_returnStatuses() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.getOrderByNumber(anyString())).thenReturn(returnedOrder);
        mockMvc.perform(get("/orders/20210107-25"))
                .andExpect(model().attributeExists("statuses"));
    }

    /* update order / status */
    @Test
    public void updateOrderByNumber_returnStatusOk() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.updateOrder(any(), any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders/20210107-25/update")
                .param("status", "NEW")
        ).andExpect(status().isOk()).andExpect(view().name("order"));
    }

    @Test
    public void updateOrderWithEmptyStatus_returnStatusBadRequest() throws Exception {
        mockMvc.perform(
                post("/orders/20210107-25/update")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    public void updateOrderWithInvalidStatus_returnStatusBadRequest() throws Exception {
        mockMvc.perform(
                post("/orders/20210107-25/update")
                        .param("status", "notEnumField")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    public void updateOrder_callBusinessLogic() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.updateOrder(any(), any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders/20210107-25/update")
                .param("status", "NEW")
        ).andExpect(status().isOk());
        verify(orderService, times(1)).updateOrder(anyString(), any(UpdateOrderDTO.class));
    }

    @Test
    public void updateOrder_returnOrderWithMessage() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.updateOrder(any(), any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders/20210107-25/update")
                .param("status", "NEW")
        ).andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("message", MESSAGE_ORDER_UPDATED));
    }

    @Test
    public void updateOrder_returnValidOrder() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.updateOrder(any(), any())).thenReturn(returnedOrder);
        MvcResult mvcResult = mockMvc.perform(post("/orders/20210107-25/update")
                .param("status", "NEW")
        ).andReturn();
        String activeResult = mvcResult.getResponse().getContentAsString();
        assertThat(activeResult).contains(String.valueOf(VALID_ORDER_ID));
        assertThat(activeResult).contains(VALID_ORDER_NUMBER);
        assertThat(activeResult).contains(VALID_ORDER_STATUS);
        assertThat(activeResult).contains(VALID_ITEM_NAME);
        assertThat(activeResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
        assertThat(activeResult).contains(String.valueOf(VALID_USER_ID));
        assertThat(activeResult).contains(VALID_USER_PHONE);
        assertThat(activeResult).contains(String.valueOf(VALID_TOTAL_PRICE.longValue()));
    }

    @Test
    public void updateNonExistentOrder_redirectOrdersWithMassage() throws Exception {
        when(orderService.updateOrder(any(), any())).thenReturn(null);
        mockMvc.perform(post("/orders/20210107-25/update")
                .param("status", "NEW")
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", MESSAGE_ORDER_NOT_FOUND));
    }

    @Test
    public void updateOrder_returnStatuses() throws Exception {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.updateOrder(any(), any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders/20210107-25/update")
                .param("status", "NEW")
        ).andExpect(model().attribute("statuses", OrderStatusDTOEnum.values()));
    }

    @Test
    public void updateOrderWithInvalidStatus_redirectOrderWithError() throws Exception {
        List<String> expectedErrors = new ArrayList<>();
        expectedErrors.add(MESSAGE_ORDER_STATUS_NOT_NULL);
        mockMvc.perform(post("/orders/20210107-25/update")
        ).andExpect(flash().attribute("errors", expectedErrors));
    }

    /* add order */
    @Test
    public void addOrder_returnStatusOk() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(content().contentTypeCompatibleWith(TEXT_HTML));
    }

    @Test
    public void addOrderWithEmptyItemAmount_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders")
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute("error", MESSAGE_ITEM_AMOUNT_NOT_EMPTY));
    }

    @Test
    public void addOrderWithEmptyItemNumber_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute("error", MESSAGE_NOT_EMPTY));
    }

    @Test
    public void addOrder_callBusinessLogic() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
        verify(orderService, times(1)).addOrder(any(AddOrderDTO.class));
    }

    @Test
    public void addOrder_returnOrder() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.addOrder(any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(model().attributeExists("order"));
    }

    @Test
    public void addOrder_returnOrderWithValidNumber() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.addOrder(any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(VALID_ORDER_NUMBER)));
    }

    @Test
    public void addOrder_returnOrderWithValidStatus() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.addOrder(any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(VALID_ORDER_STATUS)));
    }

    @Test
    public void addOrder_returnOrderWithValidItemName() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.addOrder(any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(VALID_ITEM_NAME)));
    }

    @Test
    public void addOrder_returnOrderWithValidItemAmount() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.addOrder(any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(String.valueOf(VALID_ITEM_AMOUNT))));
    }

    @Test
    public void addOrder_returnOrderWithValidTotalPrice() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        OrderDetailsDTO returnedOrder = getValidOrderDetailsDTO();
        when(orderService.addOrder(any())).thenReturn(returnedOrder);
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(String.valueOf(VALID_TOTAL_PRICE))));
    }

    @Test
    public void addOrderWithAnonymousUser_redirectIndexWithError() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        when(orderService.addOrder(any())).thenThrow(new AnonymousUserException());
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", MESSAGE_ACCESS_CLOSE));
    }

    @Test
    public void addOrderWithNonExistentItem_redirectItems() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        when(orderService.addOrder(any())).thenThrow(new ObjectDBException(MESSAGE_ITEM_NOT_FOUND));
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute("error", MESSAGE_ITEM_NOT_FOUND));
    }

    @Test
    public void addOrderWithNonExistentUserInformation_redirectItems() throws Exception, AnonymousUserException, ObjectDBException, UserInformationException {
        when(orderService.addOrder(any())).thenThrow(new UserInformationException());
        mockMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("error", MESSAGE_USER_WITHOUT_INFORMATION));
    }

    private OrderDetailsDTO getValidOrderDetailsDTO() {
        OrderDetailsDTO order = new OrderDetailsDTO();
        order.setNumber(VALID_ORDER_NUMBER);
        order.setStatus(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        order.setItemName(VALID_ITEM_NAME);
        order.setItemAmount(VALID_ITEM_AMOUNT);
        order.setCustomerId(VALID_USER_ID);
        order.setCustomerPhone(VALID_USER_PHONE);
        order.setTotalPrice(VALID_TOTAL_PRICE);
        return order;
    }

    private PaginationWithEntitiesDTO<OrderDTO> getValidOrdersByPage() {
        List<OrderDTO> orderDTOS = getValidOrderDTOS();
        return new PaginationWithEntitiesDTO<>(orderDTOS, VALID_PAGES);
    }

    private List<OrderDTO> getValidOrderDTOS() {
        return IntStream.of(1)
                .mapToObj(x -> getValidOrderDTO())
                .collect(Collectors.toList());
    }

    private OrderDTO getValidOrderDTO() {
        OrderDTO order = new OrderDTO();
        order.setId(VALID_ID);
        order.setNumber(VALID_ORDER_NUMBER);
        order.setStatus(OrderStatusDTOEnum.valueOf(VALID_ORDER_STATUS));
        order.setDateCreate(VALID_ORDER_DATE);
        order.setItemName(VALID_ITEM_NAME);
        order.setItemAmount(VALID_ITEM_AMOUNT);
        order.setTotalPrice(VALID_TOTAL_PRICE);
        return order;
    }

}