package com.gmail.petrikov05.app.web.security.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class LoginControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/login" */
    @Test
    public void getLoginPageUnauthorizedUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/login")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getLoginPageLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(
                get("/login")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getLoginPageLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(
                get("/login")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getLoginPageLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(
                get("/login")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getLoginPageLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(
                get("/login")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

}