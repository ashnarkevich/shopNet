package com.gmail.petrikov05.app.web.controller.api.integration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(roles = "SECURE_API_USER")
@TestPropertySource("/application-integration.properties")
@AutoConfigureMockMvc
class ItemAPIControllerITTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllItems_returnItems() throws Exception {
        List<ItemPreviewDTO> expectedItems = getValidItemDTOS();
        String expectedResult = objectMapper.writeValueAsString(expectedItems);
        MvcResult mvcResult = mockMvc.perform(get("/api/items")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getItemByNumber_returnItem() throws Exception {
        ItemDTO expectedItem = getValidItemDTO();
        String expectedResult = objectMapper.writeValueAsString(expectedItem);
        MvcResult mvcResult = mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void addItem_returnAddedItem() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(VALID_ITEM_NAME)).exists())
                .andExpect(jsonPath("$.number", not(null)).exists())
                .andExpect(jsonPath("$.price", is(VALID_ITEM_PRICE)).exists())
                .andExpect(jsonPath("$.description", is(VALID_ITEM_DESCRIPTION)).exists());
    }

    @Test
    public void deleteItemByNumber_returnStatusWithMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/items/" + VALID_ITEM_NUMBER)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(MESSAGE_ITEM_DELETED);
    }

    private AddItemDTO getValidAddItemDTO() {
        AddItemDTO addItem = new AddItemDTO();
        addItem.setName(VALID_ITEM_NAME);
        addItem.setPrice(VALID_ITEM_PRICE);
        addItem.setDescription(VALID_ITEM_DESCRIPTION);
        return addItem;
    }

    private ItemDTO getValidItemDTO() {
        ItemDTO item = new ItemDTO();
        item.setName(VALID_ITEM_NAME);
        item.setNumber(VALID_ITEM_NUMBER);
        item.setPrice(VALID_ITEM_PRICE);
        item.setDescription(VALID_ITEM_DESCRIPTION);
        return item;
    }

    private List<ItemPreviewDTO> getValidItemDTOS() {
        return IntStream.of(1).boxed()
                .map(x -> getValidItemPreviewDTO())
                .collect(Collectors.toList());
    }

    private ItemPreviewDTO getValidItemPreviewDTO() {
        ItemPreviewDTO item = new ItemPreviewDTO();
        item.setName(VALID_ITEM_NAME);
        item.setNumber(VALID_ITEM_NUMBER);
        item.setPrice(VALID_ITEM_PRICE);
        return item;
    }

}