package com.gmail.petrikov05.app.web.controller.security.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class ItemControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/items" */
    @Test
    public void getItemsLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/items")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getItemsLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/items")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getItemsLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/items")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getItemsLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getItemsLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/items/{number}" */
    @Test
    public void getItemLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ARTICLE_NUMBER)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getItemLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getItemLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getItemLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getItemLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/items/{number}/delete" */
    @Test
    public void deleteItemLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteItemLikeCustomerUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteItemLikeSaleUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items?page=" + VALID_PAGE));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteItemLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void deleteItemLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/items/{number}/copy" */
    @Test
    public void copyItemLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void copyItemLikeCustomerUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void copyItemLikeSaleUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk())
                .andExpect(view().name("item"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void copyItemLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void copyItemLikeApiUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

}