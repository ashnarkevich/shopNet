package com.gmail.petrikov05.app.service;

import java.util.List;

import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;

public interface ItemService {

    PaginationWithEntitiesDTO<ItemPreviewDTO> getItemsByPage(int page);

    boolean deleteItemByNumber(String number) throws ObjectDBException;

    ItemDTO copyItemByNumber(String number) throws ObjectDBException;

    ItemDTO getItemByNumber(String number) throws ObjectDBException;

    List<ItemPreviewDTO> getAllItems();

    ItemDTO addItem(AddItemDTO addItemDTO);

}
