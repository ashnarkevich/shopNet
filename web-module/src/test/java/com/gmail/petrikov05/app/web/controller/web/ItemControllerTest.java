package com.gmail.petrikov05.app.web.controller.web;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.constant.TestConstant;
import com.gmail.petrikov05.app.web.controller.ItemController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_PRICE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ItemController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "CUSTOMER_USER")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @Test
    public void getItems_returnStatusOk() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("items"));
    }

    @Test
    public void getItems_callBusinessLogic() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk());
        verify(itemService, times(1)).getItemsByPage(anyInt());
    }

    @Test
    public void getItems_returnValidPage() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        mockMvc.perform(get("/items")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(model().attribute("page", VALID_PAGE));
    }

    @Test
    public void getItems_returnValidPages() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        mockMvc.perform(get("/items"))
                .andExpect(model().attribute("pages", VALID_PAGES));
    }

    @Test
    public void getItems_returnItems() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        mockMvc.perform(get("/items"))
                .andExpect(model().attributeExists("items"));
    }

    @Test
    public void getItems_returnItemsWithValidName() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        MvcResult mvcResult = mockMvc.perform(get("/items"))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ITEM_NAME);
    }

    @Test
    public void getItems_returnItemsWithValidNumber() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        MvcResult mvcResult = mockMvc.perform(get("/items"))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ITEM_NUMBER);
    }

    @Test
    public void getItems_returnItemsWithValidPrice() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems = getValidPaginationWithItemsDTO();
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        MvcResult mvcResult = mockMvc.perform(get("/items"))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ITEM_PRICE));
    }

    @Test
    public void getNonExistentItems_returnEmptyItemsWithMessage() throws Exception {
        PaginationWithEntitiesDTO<ItemPreviewDTO> returnedPaginationWithItems =
                new PaginationWithEntitiesDTO<>(Collections.emptyList(), VALID_PAGES);
        when(itemService.getItemsByPage(anyInt())).thenReturn(returnedPaginationWithItems);
        MvcResult mvcResult = mockMvc.perform(get("/items"))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains("Items not found");
    }

    private PaginationWithEntitiesDTO<ItemPreviewDTO> getValidPaginationWithItemsDTO() {
        List<ItemPreviewDTO> itemPreviewDTOS = getValidItemPreviewDTOS();
        return new PaginationWithEntitiesDTO<>(itemPreviewDTOS, VALID_PAGES);
    }

    private List<ItemPreviewDTO> getValidItemPreviewDTOS() {
        return IntStream.of(1).mapToObj(x -> getValidItemPreviewDTO()).collect(Collectors.toList());

    }

    private ItemPreviewDTO getValidItemPreviewDTO() {
        ItemPreviewDTO item = new ItemPreviewDTO();
        item.setName(VALID_ITEM_NAME);
        item.setNumber(VALID_ITEM_NUMBER);
        item.setPrice(VALID_ITEM_PRICE);
        return item;
    }

}