package com.gmail.petrikov05.app.service.util.converter;

import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.repository.model.ItemDetails;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;

public class ItemConverter {

    public static ItemPreviewDTO convertObjectToPreviewDTO(Item item) {
        ItemPreviewDTO itemDTO = new ItemPreviewDTO();
        itemDTO.setName(item.getName());
        itemDTO.setNumber(item.getNumber());
        itemDTO.setPrice(item.getPrice());
        return itemDTO;
    }

    public static ItemDTO convertObjectToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName(item.getName());
        itemDTO.setNumber(item.getNumber());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setDescription(item.getItemDetails().getDescription());
        return itemDTO;
    }

    public static Item convertAddDTOToObject(AddItemDTO itemDTO) {
        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());

        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setItem(item);
        itemDetails.setDescription(itemDTO.getDescription());
        item.setItemDetails(itemDetails);
        return item;
    }

}
