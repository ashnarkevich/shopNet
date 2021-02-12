package com.gmail.petrikov05.app.web.controller;

import java.lang.invoke.MethodHandles;

import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_COPIED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ITEM_NOT_FOUND;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final static Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private final ItemService itemService;

    public ItemController(ItemService itemService) {this.itemService = itemService;}

    @GetMapping
    public String getItemsByPage(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model
    ) {
        PaginationWithEntitiesDTO<ItemPreviewDTO> itemsWithPagination = itemService.getItemsByPage(page);
        model.addAttribute("page", page);
        model.addAttribute("pages", itemsWithPagination.getPages());
        model.addAttribute("items", itemsWithPagination.getEntities());
        return "items";
    }

    @GetMapping("/{number}")
    public String getItemByNumber(
            @PathVariable(name = "number") String number,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            ItemDTO item = itemService.getItemByNumber(number);
            model.addAttribute("item", item);
            return "item";
        } catch (ObjectDBException e) {
            System.out.println("dfsdfjgbsjdhfbglsjfd");
            redirectAttributes.addFlashAttribute("error", MESSAGE_ITEM_NOT_FOUND);
            return "redirect:/items";
        }
    }

    @GetMapping("/{number}/delete")
    public String deleteItemByNumber(
            @PathVariable(name = "number") String number,
            @RequestParam(name = "page") Integer page,
            RedirectAttributes redirectAttributes
    ) {
        try {
            boolean isDeleted = itemService.deleteItemByNumber(number);
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("message", MESSAGE_ITEM_DELETED);
            } else {
                redirectAttributes.addFlashAttribute("error", MESSAGE_ITEM_DELETED_FAIL);
            }
            return "redirect:/items?page=" + page;
        } catch (ObjectDBException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/items?page=" + page;
        }
    }

    @GetMapping("/{number}/copy")
    public String copyItem(
            @PathVariable(name = "number") String number,
            @RequestParam(name = "page") Integer page,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            ItemDTO copiedItem = itemService.copyItemByNumber(number);
            model.addAttribute("item", copiedItem);
            logger.info("Item (" + number + "): " + MESSAGE_ITEM_COPIED);
            return "item";
        } catch (ObjectDBException e) {
            redirectAttributes.addFlashAttribute("error", MESSAGE_ITEM_NOT_FOUND);
            return "redirect:/items?page=" + page;
        }
    }

}
