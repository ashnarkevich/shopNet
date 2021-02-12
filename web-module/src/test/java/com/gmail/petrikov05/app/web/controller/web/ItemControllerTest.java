package com.gmail.petrikov05.app.web.controller.web;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.controller.ItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_DESCRIPTION;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_PRICE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ItemController.class)
@Import(TestConfig.class)
@WithMockUser(roles = {"CUSTOMER_USER", "SALE_USER"})
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    /* start get items by page */
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
    /* finish get items by page */

    /* start delete item by number */
    @Test
    public void deleteItemByNumber_redirectItems() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenReturn(true);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items?page=" + VALID_PAGE));
    }

    @Test
    public void deleteItemByNumber_callBusinessLogic() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenReturn(true);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection());
        verify(itemService, times(1)).deleteItemByNumber(anyString());
    }

    @Test
    public void deleteItemByNumberWithPage_redirectWithValidPage() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items?page=" + VALID_PAGE));
    }

    @Test
    public void deleteItemByValidNumber_redirectWithMessage() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenReturn(true);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", MESSAGE_ITEM_DELETED));
    }

    @Test
    public void deleteInvalidItemByNumber_redirectWithError() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenReturn(false);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", MESSAGE_ITEM_DELETED_FAIL));
    }

    @Test
    public void deleteNonExistentItem_redirectItemsWithError() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenThrow(new ObjectDBException("The item not found."));
        mockMvc.perform(get("/items/invalid/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items?page=" + VALID_PAGE))
                .andExpect(flash().attribute("error", MESSAGE_ITEM_NOT_FOUND));
    }
    /* finish delete item by number */

    /* start copy item */
    @Test
    public void copyValidItem_returnStatusOk() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.copyItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk())
                .andExpect(view().name("item"));
    }

    @Test
    public void copyValidItem_callBusinessLogin() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.copyItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk());
        verify(itemService, times(1)).copyItemByNumber(anyString());
    }

    @Test
    public void copyValidItem_returnItem() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.copyItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(model().attributeExists("item"));
    }

    @Test
    public void copyValidItem_returnItemWithValidName() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.copyItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(content().string(containsString(VALID_ITEM_NAME)));
    }

    @Test
    public void copyValidItem_returnItemWithValidNumber() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.copyItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(content().string(containsString(VALID_ITEM_NUMBER)));
    }

    @Test
    public void copyValidItem_returnItemWithValidPrice() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.copyItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(content().string(containsString(String.valueOf(VALID_ITEM_PRICE))));
    }

    @Test
    public void copyValidItem_returnItemWithValidDescription() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.copyItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(content().string(containsString(VALID_ITEM_DESCRIPTION)));
    }

    @Test
    public void copyNonExistentItem_redirectItemsWithPageWithError() throws Exception, ObjectDBException {
        when(itemService.copyItemByNumber(anyString())).thenThrow(new ObjectDBException("The item not found."));
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER + "/copy")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items?page=" + VALID_PAGE));
    }
    /* finish copy item */

    /* start get item by number */
    @Test
    public void getItem_returnStatusOk() throws Exception {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    public void getItem_callBusinessLogic() throws Exception, ObjectDBException {
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
        verify(itemService, times(1)).getItemByNumber(anyString());
    }

    @Test
    public void getItem_returnItem() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(model().attributeExists("item"));
    }

    @Test
    public void getItem_returnItemWithValidName() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(VALID_ITEM_NAME)));
    }

    @Test
    public void getItem_returnItemWithValidNumber() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(VALID_ITEM_NUMBER)));
    }

    @Test
    public void getItem_returnItemWithValidPrice() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(String.valueOf(VALID_ITEM_PRICE))));
    }

    @Test
    public void getItem_returnItemWithValidDescription() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString(VALID_ITEM_DESCRIPTION)));
    }

    @Test
    public void getNonExistentItem_redirectItemsWithError() throws Exception, ObjectDBException {
        when(itemService.getItemByNumber(anyString())).thenThrow(new ObjectDBException("The item not found"));
        mockMvc.perform(get("/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attributeExists("error"));
    }
    /* finish get item by number */

    private ItemDTO getValidItemDTO() {
        ItemDTO item = new ItemDTO();
        item.setName(VALID_ITEM_NAME);
        item.setNumber(VALID_ITEM_NUMBER);
        item.setPrice(VALID_ITEM_PRICE);
        item.setDescription(VALID_ITEM_DESCRIPTION);
        return item;
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