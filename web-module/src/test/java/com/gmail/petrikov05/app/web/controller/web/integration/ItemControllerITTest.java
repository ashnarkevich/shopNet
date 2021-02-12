package com.gmail.petrikov05.app.web.controller.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_DESCRIPTION;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = {"CUSTOMER_USER", "SALE_USER"})
public class ItemControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getItems_returnItems() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/items")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ITEM_NAME);
        assertThat(actualResult).contains(VALID_ITEM_NUMBER);
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_PRICE));
    }

    @Test
    public void getItemByNumber_returnItem() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(VALID_ITEM_NAME)))
                .andExpect(content().string(containsString(VALID_ITEM_NUMBER)))
                .andExpect(content().string(containsString(String.valueOf(VALID_ITEM_PRICE))))
                .andExpect(content().string(containsString(VALID_ITEM_DESCRIPTION)));
    }

    @Test
    public void deleteItemByNumber_returnItemsByPage() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
                .param("page", "1")
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", MESSAGE_ITEM_DELETED));
    }

    @Test
    public void copyItem_returnItemsByPage() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", "1")
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(VALID_ITEM_NAME + "-copy")))
                .andExpect(content().string(containsString(VALID_ITEM_DESCRIPTION)));
    }

}
