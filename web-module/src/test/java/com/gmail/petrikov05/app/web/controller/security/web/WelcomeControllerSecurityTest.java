package com.gmail.petrikov05.app.web.controller.security.web;

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
class WelcomeControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getReviewsLikeUnauthorizedUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getReviewsLikeAdministratorUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getReviewsLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getReviewsLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getReviewsLikeApiUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/")
        ).andExpect(status().isOk());
    }

}