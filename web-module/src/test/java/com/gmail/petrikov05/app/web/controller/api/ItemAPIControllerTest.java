package com.gmail.petrikov05.app.web.controller.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage.MESSAGE_NAME_NOT_EMPTY;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_DESCRIPTION;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ITEM_PRICE;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemAPIController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "SECURE_API_USER")
@AutoConfigureMockMvc
class ItemAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    /* start get all items */
    @Test
    public void getAllItems_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/items")
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void getAllItems_callBusinessLogic() throws Exception {
        mockMvc.perform(get("/api/items")
        ).andExpect(status().isOk());
        verify(itemService, times(1)).getAllItems();
    }

    @Test
    public void getAllItems_returnItemWithVailName() throws Exception {
        List<ItemPreviewDTO> returnedItems = getValidItemPreviewDTOS();
        when(itemService.getAllItems()).thenReturn(returnedItems);
        mockMvc.perform(get("/api/items")
        ).andExpect(jsonPath("$[0].name", is(VALID_ITEM_NAME)).exists());
    }

    @Test
    public void getAllItems_returnItemWithVailNumber() throws Exception {
        List<ItemPreviewDTO> returnedItems = getValidItemPreviewDTOS();
        when(itemService.getAllItems()).thenReturn(returnedItems);
        mockMvc.perform(get("/api/items")
        ).andExpect(jsonPath("$[0].number", is(VALID_ITEM_NUMBER)).exists());
    }

    @Test
    public void getAllItems_returnItemWithVailPrice() throws Exception {
        List<ItemPreviewDTO> returnedItems = getValidItemPreviewDTOS();
        when(itemService.getAllItems()).thenReturn(returnedItems);
        mockMvc.perform(get("/api/items")
        ).andExpect(jsonPath("$[0].price", is(VALID_ITEM_PRICE)).exists());
    }
    /* finish get all items */

    /* start get item by number */
    @Test
    public void getItemByNumber_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    public void getItemByNumber_callBusinessLogic() throws Exception, ObjectDBException {
        mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
        verify(itemService, times(1)).getItemByNumber(anyString());
    }

    @Test
    public void getItemByNumber_returnItemWithValidName() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(jsonPath("$.name", is(VALID_ITEM_NAME)).exists());
    }

    @Test
    public void getItemByNumber_returnItemWithValidNumber() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(jsonPath("$.number", is(VALID_ITEM_NUMBER)).exists());
    }

    @Test
    public void getItemByNumber_returnItemWithValidPrice() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(jsonPath("$.price", is(VALID_ITEM_PRICE)).exists());
    }

    @Test
    public void getItemByNumber_returnItemWithValidDescription() throws Exception, ObjectDBException {
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.getItemByNumber(anyString())).thenReturn(returnedItem);
        mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(jsonPath("$.description", is(VALID_ITEM_DESCRIPTION)).exists());
    }

    @Test
    public void getNonExistentItem_returnErrorMessage() throws Exception, ObjectDBException {
        when(itemService.getItemByNumber(anyString())).thenThrow(new ObjectDBException("Item not found"));
        mockMvc.perform(get("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(content().string(containsString("Item not found")));
    }
    /* finish get item by number */

    /* start add item */
    @Test
    public void addItem_returnStatusCreated() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    public void addInvalidItem_returnStatusBadRequest() throws Exception {
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItemWithEmptyName_returnStatusBadRequest() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setName("");
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItemWithLongName_returnStatusBadRequest() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setName(getStringByLength(101));
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItemWithEmptyPrice_returnStatusBadRequest() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setPrice(null);
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItemWithSmallPrice_returnStatusBadRequest() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setPrice(BigDecimal.valueOf(0L));
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItemWithLongPrice_returnStatusBadRequest() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setPrice(BigDecimal.valueOf(10001L));
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItemWithEmptyDescription_returnStatusBadRequest() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setDescription("");
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItemWithLongDescription_returnStatusBadRequest() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setDescription(getStringByLength(201));
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addItem_callBusinessLogic() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isCreated());
        verify(itemService, times(1)).addItem(any());
    }

    @Test
    public void addItem_returnItemWithValidName() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.addItem(any())).thenReturn(returnedItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(jsonPath("$.name", is(VALID_ITEM_NAME)).exists());
    }

    @Test
    public void addItem_returnItemWithValidNumber() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.addItem(any())).thenReturn(returnedItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(jsonPath("$.number", is(VALID_ITEM_NUMBER)).exists());
    }

    @Test
    public void addItem_returnItemWithValidPrice() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.addItem(any())).thenReturn(returnedItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(jsonPath("$.price", is(VALID_ITEM_PRICE)).exists());
    }

    @Test
    public void addItem_returnItemWithValidDescription() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        String content = objectMapper.writeValueAsString(addItem);
        ItemDTO returnedItem = getValidItemDTO();
        when(itemService.addItem(any())).thenReturn(returnedItem);
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(jsonPath("$.description", is(VALID_ITEM_DESCRIPTION)).exists());
    }

    @Test
    public void addInvalidItem_returnBadRequestWithErrorMessage() throws Exception {
        AddItemDTO addItem = getValidAddItemDTO();
        addItem.setName("");
        String content = objectMapper.writeValueAsString(addItem);

        MvcResult mvcResult = mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();

        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        List<String> errors = new ArrayList<>();
        errors.add(MESSAGE_NAME_NOT_EMPTY);
        responseError.setErrors(errors);
        String expectedResult = objectMapper.writeValueAsString(responseError);
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }
    /* finish add item */

    /* start delete item by number */
    @Test
    public void deleteItem_returnStatusOk() throws Exception {
        mockMvc.perform(delete("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteItem_callBusinessLogic() throws Exception, ObjectDBException {
        mockMvc.perform(delete("/api/items/" + VALID_ITEM_NUMBER)
        ).andExpect(status().isOk());
        verify(itemService, times(1)).deleteItemByNumber(anyString());
    }

    @Test
    public void deleteValidItem_returnPositiveMessage() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenReturn(true);
        MvcResult mvcResult = mockMvc.perform(delete("/api/items/" + VALID_ITEM_NUMBER)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult).isEqualTo(MESSAGE_ITEM_DELETED);
    }

    @Test
    public void deleteInvalidItem_returnNegativeMessage() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenReturn(false);
        MvcResult mvcResult = mockMvc.perform(delete("/api/items/" + VALID_ITEM_NUMBER)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult).isEqualTo(MESSAGE_ITEM_DELETED_FAIL);
    }

    @Test
    public void deleteNonExistentItem_returnNegativeMessage() throws Exception, ObjectDBException {
        when(itemService.deleteItemByNumber(anyString())).thenThrow(new ObjectDBException("Item not found"));
        MvcResult mvcResult = mockMvc.perform(delete("/api/items/" + VALID_ITEM_NUMBER)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult).isEqualTo(MESSAGE_ITEM_NOT_FOUND);
    }
    /* finish delete item by number */

    private String getStringByLength(int length) {
        return IntStream.range(0, length)
                .boxed()
                .map(x -> "a")
                .collect(Collectors.joining());
    }

    private AddItemDTO getValidAddItemDTO() {
        AddItemDTO item = new AddItemDTO();
        item.setName(VALID_ITEM_NAME);
        item.setPrice(VALID_ITEM_PRICE);
        item.setDescription(VALID_ITEM_DESCRIPTION);
        return item;
    }

    private ItemDTO getValidItemDTO() {
        ItemDTO item = new ItemDTO();
        item.setName(VALID_ITEM_NAME);
        item.setNumber(VALID_ITEM_NUMBER);
        item.setPrice(VALID_ITEM_PRICE);
        item.setDescription(VALID_ITEM_DESCRIPTION);
        return item;
    }

    private List<ItemPreviewDTO> getValidItemPreviewDTOS() {
        return IntStream.of(1)
                .boxed()
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