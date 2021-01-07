package com.gmail.petrikov05.app.web.controller.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class LoginControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getLoginPage() throws Exception {
        mockMvc.perform(
                get("/login")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void get403Page() throws Exception {
        mockMvc.perform(
                get("/403")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(view().name("403"));
    }

}