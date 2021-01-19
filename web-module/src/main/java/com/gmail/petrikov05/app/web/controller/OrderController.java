package com.gmail.petrikov05.app.web.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.gmail.petrikov05.app.service.OrderService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.exception.UserInformationException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.order.AddOrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDTO;
import com.gmail.petrikov05.app.service.model.order.OrderDetailsDTO;
import com.gmail.petrikov05.app.service.model.order.OrderStatusDTOEnum;
import com.gmail.petrikov05.app.service.model.order.UpdateOrderDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ORDER_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ORDER_UPDATED;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @GetMapping
    public String getOrdersByPage(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            PaginationWithEntitiesDTO<OrderDTO> ordersWithPagination = orderService.getOrdersByPage(page);
            model.addAttribute("page", page);
            model.addAttribute("pages", ordersWithPagination.getPages());
            model.addAttribute("orders", ordersWithPagination.getEntities());
            return "orders";
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/{number}")
    public String getOrderByNumber(
            @PathVariable String number,
            Model model
    ) {
        OrderDetailsDTO order = orderService.getOrderByNumber(number);
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatusDTOEnum.values());
        return "order";
    }

    @PostMapping("/{number}/update")
    public String updateStatusByNumber(
            @PathVariable String number,
            RedirectAttributes redirectAttributes,
            @ModelAttribute(name = "order") @Valid UpdateOrderDTO updateOrder,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = getErrors(bindingResult);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/orders/" + number;
        }
        OrderDetailsDTO updatedOrder = orderService.updateOrder(number, updateOrder);
        if (updatedOrder == null) {
            redirectAttributes.addFlashAttribute("error", MESSAGE_ORDER_NOT_FOUND);
            return "redirect:/orders";
        }
        model.addAttribute("order", updatedOrder);
        model.addAttribute("statuses", OrderStatusDTOEnum.values());
        model.addAttribute("message", MESSAGE_ORDER_UPDATED);
        return "order";
    }

    @PostMapping
    public String addOrder(
            @ModelAttribute @Valid AddOrderDTO addOrder,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining());
            redirectAttributes.addFlashAttribute("error", errors);
            return "redirect:/items";
        }
        try {
            OrderDetailsDTO addedOrder = orderService.addOrder(addOrder);
            model.addAttribute("order", addedOrder);
            return "order";
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        } catch (ObjectDBException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/items";
        } catch (UserInformationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile";
        }
    }

    private List<String> getErrors(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(x -> errors.add(x.getDefaultMessage()));
        return errors;
    }

}
