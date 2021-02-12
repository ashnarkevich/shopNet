package com.gmail.petrikov05.app.web.controller.api;

import java.util.List;
import javax.validation.Valid;

import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_NOT_FOUND;

@RestController
@RequestMapping("/api/items")
public class ItemAPIController {

    private final ItemService itemService;

    public ItemAPIController(ItemService itemService) {this.itemService = itemService;}

    @GetMapping
    public List<ItemPreviewDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{number}")
    public Object getItemByNumber(
            @PathVariable String number
    ) {
        try {
            return itemService.getItemByNumber(number);
        } catch (ObjectDBException e) {
            return e.getMessage();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object addItem(
            @RequestBody @Valid AddItemDTO addItemDTO
    ) {
        return itemService.addItem(addItemDTO);
    }

    @DeleteMapping("/{number}")
    public String deleteItemByNumber(
            @PathVariable String number
    ) {
        try {
            boolean isDeleted = itemService.deleteItemByNumber(number);
            if (isDeleted) {
                return MESSAGE_ITEM_DELETED;
            } else {
                return MESSAGE_ITEM_DELETED_FAIL;
            }
        } catch (ObjectDBException e) {
            return MESSAGE_ITEM_NOT_FOUND;
        }
    }

}
