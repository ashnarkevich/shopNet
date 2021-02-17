package com.gmail.petrikov05.app.web.security.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_AMOUNT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ORDER_STATUS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class OrderControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/orders" */
    @Test
    public void getOrdersLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/orders")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "best@best.com", roles = "SALE_USER")
    public void getOrdersLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/orders")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "best@best.com", roles = "CUSTOMER_USER")
    public void getOrdersLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/orders")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getOrdersLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/orders")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getOrdersLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/orders")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/orders/{number}" */
    @Test
    public void getOrderByNumberLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/orders/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getOrderByNumberLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/orders/" + VALID_ORDER_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getOrderByNumberLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/orders/1")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getOrderByNumberLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/orders/1")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getOrderByNumberLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/orders/1")
        ).andExpect(status().isOk());
    }

    /* post "/orders/{number}/update" */
    @Test
    public void updateOrderByNumberLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/orders/" + VALID_ORDER_NUMBER + "/update")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void updateOrderByNumberLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/orders/" + VALID_ORDER_NUMBER + "/update")
                .param("status", VALID_ORDER_STATUS)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void updateOrderByNumberLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders/" + VALID_ORDER_NUMBER + "/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void updateOrderByNumberLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders/" + VALID_ORDER_NUMBER + "/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void updateOrderByNumberLikeCustomerUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders/" + VALID_ORDER_NUMBER + "/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/orders" */
    @Test
    public void addOrderLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/orders")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void addOrderLikeUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void addOrderLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void addOrderLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void addOrderLikeCustomerUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/orders")
                .param("itemNumber", VALID_ITEM_NUMBER)
                .param("itemAmount", String.valueOf(VALID_ITEM_AMOUNT))
        ).andExpect(status().isOk());
    }

}