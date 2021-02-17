package com.gmail.petrikov05.app.web.security.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class OrderApiControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/orders" */
    @Test
    public void getOrdersLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/orders")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getOrdersLikeApiUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/orders")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getOrdersLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/orders")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getOrdersLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/orders")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getOrdersLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/orders")
        ).andExpect(status().isForbidden());
    }

    /* get "/orders/{number}" */
    @Test
    public void getOrderByNumberLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/orders/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getOrderByNumberLikeApiUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/orders/1")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getOrderByNumberLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/orders/1")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getOrderByNumberLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/orders/1")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getOrderByNumberLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/orders/1")
        ).andExpect(status().isForbidden());
    }

}