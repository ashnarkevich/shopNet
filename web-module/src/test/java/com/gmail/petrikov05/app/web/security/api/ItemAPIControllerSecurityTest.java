package com.gmail.petrikov05.app.web.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_DESCRIPTION;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_PRICE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class ItemAPIControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    /* get "/api/items" */
    @Test
    public void getItemsUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/items")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getItemsAuthorizedUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getItemsLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getItemsLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getItemsLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items")
        ).andExpect(status().isForbidden());
    }

    /* post "/api/items */
    @Test
    public void addItemLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/api/items")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void addItemLikeApiUser_returnStatusCreated() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void addItemLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/items")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void addItemLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/items")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void addItemLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/items")
        ).andExpect(status().isForbidden());
    }

    /* get "/api/items/{id}" */
    @Test
    public void getItemLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "SECURE_API_USER")
    public void getItemLikeApiUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getItemLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getItemLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getItemLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isForbidden());
    }

    /* delete "/api/items/{id}" */
    @Test
    public void deleteItemLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "SECURE_API_USER")
    public void deleteItemLikeApiUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteItemLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteItemLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteItemLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/items/1")
        ).andExpect(status().isForbidden());
    }

    private AddItemDTO getValidAddItemDTO() {
        AddItemDTO addItem = new AddItemDTO();
        addItem.setName(VALID_ITEM_NAME);
        addItem.setDescription(VALID_ITEM_DESCRIPTION);
        addItem.setPrice(VALID_ITEM_PRICE);
        return addItem;
    }

}
