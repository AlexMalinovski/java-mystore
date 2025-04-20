package ru.practicum.mystore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.mystore.data.dto.CartDto;
import ru.practicum.mystore.data.dto.OrderDto;
import ru.practicum.mystore.service.CartService;
import ru.practicum.mystore.service.OrderService;

import java.util.List;

@Controller
@SessionAttributes({"orderId"})
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping(StoreUrls.Orders.FULL)
    String placeOrder(Model model, WebRequest request, SessionStatus status) {
        final Long orderId = (Long) model.asMap().get("orderId");
        if (orderId == null) {
            return "redirect:" + StoreUrls.Main.FULL;
        }
        orderService.placeOrder(orderId);

        status.setComplete();
        request.removeAttribute("orderId", WebRequest.SCOPE_SESSION);

        return "redirect:" + StoreUrls.Orders.OrderId.FULL.replaceAll("\\{orderId}", String.valueOf(orderId)) + "?isNew=true";
    }

    @GetMapping(StoreUrls.Orders.OrderId.FULL)
    String getOrderById(
            @PathVariable long orderId,
            @RequestParam(required = false, defaultValue = "false") boolean isNew,
            Model model) {

        model.addAttribute("isneworder", isNew);
        CartDto cart = cartService.getOrderCart(orderId);
        model.addAttribute("cart", cart);

        return "order";
    }

    @GetMapping(StoreUrls.Orders.FULL)
    String getOrders(Model model) {
        List<OrderDto> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

}
