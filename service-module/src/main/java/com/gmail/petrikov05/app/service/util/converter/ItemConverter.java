package com.gmail.petrikov05.app.service.util.converter;

import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;

public class ItemConverter {

    public static ItemPreviewDTO convertObjectToPreviewDTO(Item item) {
        ItemPreviewDTO itemDTO = new ItemPreviewDTO();
        itemDTO.setName(item.getName());
        itemDTO.setNumber(item.getNumber());
        itemDTO.setPrice(item.getPrice());
        return itemDTO;
    }

}
