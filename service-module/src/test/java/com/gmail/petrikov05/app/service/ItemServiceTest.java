package com.gmail.petrikov05.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.repository.ItemRepository;
import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.repository.model.ItemDetails;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.impl.ItemServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COUNT_OF_ENTITIES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_DESCRIPTION;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_PRICE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_OBJECT_BY_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_START_POSITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {this.itemService = new ItemServiceImpl(itemRepository);}

    /* start get items by page */
    @Test
    public void getItemsWithPagination_returnValidItems() {
        List<Item> returnedItems = getValidItems();
        when(itemRepository.getItemsByPage(VALID_START_POSITION, VALID_OBJECT_BY_PAGE)).thenReturn(returnedItems);
        PaginationWithEntitiesDTO<ItemPreviewDTO> actualResult = itemService.getItemsByPage(VALID_PAGE);
        verify(itemRepository, times(1)).getItemsByPage(anyInt(), anyInt());
        assertThat(actualResult.getEntities()).isNotNull();
        assertThat(actualResult.getEntities().get(0).getName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getEntities().get(0).getNumber()).isEqualTo(VALID_ITEM_NUMBER);
        assertThat(actualResult.getEntities().get(0).getPrice()).isEqualTo(VALID_ITEM_PRICE);
    }

    @Test
    public void getItemsWithPagination_returnValidPages() {
        when(itemRepository.getCountOfEntities()).thenReturn(VALID_COUNT_OF_ENTITIES);
        PaginationWithEntitiesDTO<ItemPreviewDTO> actualResult = itemService.getItemsByPage(VALID_PAGE);
        verify(itemRepository, times(1)).getCountOfEntities();
        assertThat(actualResult.getPages()).isEqualTo(VALID_PAGES);
    }
    /* finish get items by page */

    /* start delete item by page */
    @Test
    public void deleteItemByValidNumber_returnTrue() throws ObjectDBException {
        Optional<Item> returnedItem = Optional.of(getValidItem());
        when(itemRepository.getItemByNumber(anyString())).thenReturn(returnedItem);
        when(itemRepository.delete(any())).thenReturn(true);
        boolean actualResult = itemService.deleteItemByNumber(VALID_ITEM_NUMBER);
        verify(itemRepository, times(1)).getItemByNumber(anyString());
        verify(itemRepository, times(1)).delete(any());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isTrue();
    }

    @Test
    public void deleteNonExistentItem_returnObjectDBException() throws ObjectDBException {
        when(itemRepository.getItemByNumber(anyString())).thenReturn(Optional.empty());
        assertThatExceptionOfType(ObjectDBException.class)
                .isThrownBy(() -> itemService.deleteItemByNumber(anyString()));
        assertThrows(ObjectDBException.class, () -> itemService.deleteItemByNumber(anyString()));
    }
    /* finish delete item by page */

    /* start copy item by number */
    @Test
    public void copyValidItem_returnValidItem() throws ObjectDBException {
        Item returnedItem = getValidItem();
        Optional<Item> returnedOptionalItem = Optional.of(returnedItem);
        when(itemRepository.getItemByNumber(anyString())).thenReturn(returnedOptionalItem);
        Item copyItem = getValidItem();
        when(itemRepository.add(any())).thenReturn(copyItem);
        ItemDTO actualResult = itemService.copyItemByNumber(VALID_ITEM_NUMBER);
        verify(itemRepository, times(1)).getItemByNumber(VALID_ITEM_NUMBER);
        verify(itemRepository, times(1)).add(any());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getNumber()).isNotEqualTo(VALID_ITEM_NUMBER);
        assertThat(actualResult.getPrice()).isEqualTo(VALID_ITEM_PRICE);
        assertThat(actualResult.getDescription()).isEqualTo(VALID_ITEM_DESCRIPTION);
    }

    @Test
    public void copyValidItem_returnItemWithValidNewName() throws ObjectDBException {
        Item returnedItem = getValidItem();
        Optional<Item> returnedOptionalItem = Optional.of(returnedItem);
        when(itemRepository.getItemByNumber(anyString())).thenReturn(returnedOptionalItem);
        Long returnedCountItems = 3L;
        when(itemRepository.getCountItemByName(anyString())).thenReturn(returnedCountItems);
        ItemDTO actualResult = itemService.copyItemByNumber(VALID_ITEM_NUMBER);
        verify(itemRepository, times(1)).getCountItemByName(anyString());
        assertThat(actualResult.getName()).isEqualTo(VALID_ITEM_NAME + "-copy(3)");
    }

    @Test
    public void copyNonExistentItem_returnObjectDBException() {
        when(itemRepository.getItemByNumber(anyString())).thenReturn(Optional.empty());
        assertThatExceptionOfType(ObjectDBException.class)
                .isThrownBy(() -> itemService.copyItemByNumber(anyString()));
        assertThrows(ObjectDBException.class, () -> itemService.copyItemByNumber(anyString()));
    }
    /* finish copy item by number */

    /* start get item by number */
    @Test
    public void getItemByNumber_returnValidItem() throws ObjectDBException {
        Item returnedItem = getValidItem();
        Optional<Item> returnedOptionItem = Optional.of(returnedItem);
        when(itemRepository.getItemByNumber(anyString())).thenReturn(returnedOptionItem);
        ItemDTO actualResult = itemService.getItemByNumber(VALID_ITEM_NUMBER);
        verify(itemRepository, times(1)).getItemByNumber(anyString());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getNumber()).isEqualTo(VALID_ITEM_NUMBER);
        assertThat(actualResult.getPrice()).isEqualTo(VALID_ITEM_PRICE);
        assertThat(actualResult.getDescription()).isEqualTo(VALID_ITEM_DESCRIPTION);
    }

    @Test
    public void getNonExistentItem_returnObjectDBException() {
        when(itemRepository.getItemByNumber(anyString())).thenReturn(Optional.empty());
        assertThatExceptionOfType(ObjectDBException.class)
                .isThrownBy(() -> itemService.getItemByNumber(anyString()));
        assertThrows(ObjectDBException.class, () -> itemService.getItemByNumber(anyString()));
    }
    /* finish get item by number */

    /* start get all items */
    @Test
    public void getAllItems_returnListWithValidItem() {
        List<Item> returnedItems = getValidItems();
        when(itemRepository.getAllObjects()).thenReturn(returnedItems);
        List<ItemPreviewDTO> actualResult = itemService.getAllItems();
        verify(itemRepository, times(1)).getAllObjects();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.get(0).getName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.get(0).getNumber()).isEqualTo(VALID_ITEM_NUMBER);
        assertThat(actualResult.get(0).getPrice()).isEqualTo(VALID_ITEM_PRICE);
    }
    /* finish get all items */

    /* start add item */
    @Test
    public void addItem_returnAddedItem() {
        AddItemDTO addItem = getValidAddItemDTO();
        Item returnedItem = getValidItem();
        when(itemRepository.add(any())).thenReturn(returnedItem);
        ItemDTO actualResult = itemService.addItem(addItem);
        verify(itemRepository, times(1)).add(any());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getName()).isEqualTo(VALID_ITEM_NAME);
        assertThat(actualResult.getNumber()).isEqualTo(VALID_ITEM_NUMBER);
        assertThat(actualResult.getPrice()).isEqualTo(VALID_ITEM_PRICE);
        assertThat(actualResult.getDescription()).isEqualTo(VALID_ITEM_DESCRIPTION);
    }
    /* finish add item */

    private AddItemDTO getValidAddItemDTO() {
        AddItemDTO item = new AddItemDTO();
        item.setName(VALID_ITEM_NAME);
        item.setPrice(VALID_ITEM_PRICE);
        item.setDescription(VALID_ITEM_DESCRIPTION);
        return item;
    }

    private List<Item> getValidItems() {
        return IntStream.of(1).mapToObj(x -> getValidItem()).collect(Collectors.toList());
    }

    private Item getValidItem() {
        Item item = new Item();
        item.setName(VALID_ITEM_NAME);
        item.setNumber(VALID_ITEM_NUMBER);
        item.setPrice(VALID_ITEM_PRICE);
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setDescription(VALID_ITEM_DESCRIPTION);
        item.setItemDetails(itemDetails);
        return item;
    }

}