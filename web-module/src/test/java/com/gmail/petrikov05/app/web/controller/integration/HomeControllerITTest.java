package com.gmail.petrikov05.app.web.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class HomeControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getHomePage() throws Exception {
        mockMvc.perform(
                get("/home")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}