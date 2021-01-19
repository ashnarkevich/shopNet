package com.gmail.petrikov05.app.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.repository.ItemRepository;
import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.service.impl.ItemServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COUNT_OF_ENTITIES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_NUMBER;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ITEM_PRICE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_OBJECT_BY_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_START_POSITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
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

    private List<Item> getValidItems() {
        return IntStream.of(1).mapToObj(x -> getValidItem()).collect(Collectors.toList());
    }

    private Item getValidItem() {
        Item item = new Item();
        item.setName(VALID_ITEM_NAME);
        item.setNumber(VALID_ITEM_NUMBER);
        item.setPrice(VALID_ITEM_PRICE);
        return item;
    }

}