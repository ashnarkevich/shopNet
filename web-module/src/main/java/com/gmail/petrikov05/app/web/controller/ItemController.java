package com.gmail.petrikov05.app.web.controller;

import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {this.itemService = itemService;}

    @GetMapping
    public String getItems(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model
    ) {
        PaginationWithEntitiesDTO<ItemPreviewDTO> itemsWithPagination = itemService.getItemsByPage(page);
        model.addAttribute("page", page);
        model.addAttribute("pages", itemsWithPagination.getPages());
        model.addAttribute("items", itemsWithPagination.getEntities());
        return "items";
    }

}
