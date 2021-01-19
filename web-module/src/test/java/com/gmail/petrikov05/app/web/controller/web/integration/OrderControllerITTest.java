package com.gmail.petrikov05.app.web.controller.web.integration;

import java.util.Locale;

import com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum;
import com.gmail.petrikov05.app.web.constant.TestConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum.NEW;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_AMOUNT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_STATUS;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_TOTAL_PRICE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(roles = {"SALE_USER", "CUSTOMER_USER"})
@TestPropertySource("/application-integration.properties")
public class OrderControllerITTest {

    @Autowired
    private MockMvc mocMvc;

    @Test
    @WithMockUser(username = "best@best.com", roles = "SALE_USER")
    public void getOrders_returnOrdersWithPagination() throws Exception {
        MvcResult mvcResult = mocMvc.perform(
                get("/orders")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
        assertThat(actualResult).contains(VALID_ORDER_NUMBER);
        assertThat(actualResult).contains(VALID_ORDER_STATUS);
        assertThat(actualResult).contains(String.valueOf(VALID_ORDER_DATE));
        assertThat(actualResult).contains(VALID_ITEM_NAME);
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
        assertThat(actualResult).contains(String.valueOf(VALID_TOTAL_PRICE));
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void getOrdersByUserByCustomer_returnOrders() throws Exception {
        MvcResult mvcResult = mocMvc.perform(get("/orders")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(TestConstant.VALID_ORDER_ID));
        assertThat(actualResult).contains(VALID_ORDER_NUMBER);
        assertThat(actualResult).contains(NEW.name());
        assertThat(actualResult).contains(VALID_ITEM_NAME);
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
        assertThat(actualResult).contains(String.valueOf(VALID_TOTAL_PRICE));
    }

    @Test
    public void getOrder_returnOrder() throws Exception {
        MvcResult mvcResult = mocMvc.perform(
                get("/orders/" + VALID_ORDER_NUMBER)
        ).andExpect(status().isOk()).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(TestConstant.VALID_ORDER_ID));
        assertThat(actualResult).contains(VALID_ORDER_NUMBER);
        assertThat(actualResult).contains(VALID_ORDER_STATUS);
        assertThat(actualResult).contains(VALID_ITEM_NAME);
        assertThat(actualResult).contains(String.valueOf(VALID_USER_ID));
        assertThat(actualResult).contains(VALID_USER_PHONE);
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
        assertThat(actualResult).contains(String.valueOf(VALID_TOTAL_PRICE));
    }

    @Test
    public void updateOrder_returnOrder() throws Exception {
        MvcResult mvcResult = mocMvc.perform(
                post("/orders/" + VALID_ORDER_NUMBER + "/update")
                        .param("status", String.valueOf(OrderStatusDTOEnum.DELIVERED))
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(TestConstant.VALID_ORDER_ID));
        assertThat(actualResult).contains(VALID_ORDER_NUMBER);
        assertThat(actualResult).contains(OrderStatusDTOEnum.DELIVERED.name());
        assertThat(actualResult).contains(VALID_ITEM_NAME);
        assertThat(actualResult).contains(String.valueOf(VALID_USER_ID));
        assertThat(actualResult).contains(VALID_USER_PHONE);
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
        assertThat(actualResult).contains(String.valueOf(VALID_TOTAL_PRICE));
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void addOrder_returnAddedOrder() throws Exception {
        MvcResult mvcResult = mocMvc.perform(post("/orders")
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
                .param("itemNumber", VALID_ITEM_NUMBER.toLowerCase(Locale.ROOT))
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(TestConstant.VALID_ORDER_ID));
        assertThat(actualResult).contains("1-2021");
        assertThat(actualResult).contains(String.valueOf(NEW));
        assertThat(actualResult).contains(VALID_ITEM_NAME);
        assertThat(actualResult).contains(String.valueOf(VALID_USER_ID));
        assertThat(actualResult).contains(VALID_USER_PHONE);
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_AMOUNT));
        assertThat(actualResult).contains(String.valueOf(VALID_TOTAL_PRICE));
    }

}
