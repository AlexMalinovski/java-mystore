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
import reactor.core.publisher.Mono;
import ru.practicum.mystore.service.CartService;
import ru.practicum.mystore.service.OrderService;

@Controller
@SessionAttributes({"orderId"})
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping(StoreUrls.Orders.FULL)
    Mono<String> placeOrder(Model model, SessionStatus status) {
        final Long orderId = (Long) model.asMap().get("orderId");
        if (orderId == null) {
            return Mono.just("redirect:" + StoreUrls.Main.FULL);
        }

        return orderService.placeOrder(orderId)
                .doOnNext(order -> {
                    status.setComplete();
                })
                .map(order -> "redirect:" + StoreUrls.Orders.OrderId.FULL.replaceAll(
                        "\\{orderId}", String.valueOf(order)) + "?isNew=true");
    }

    @GetMapping(StoreUrls.Orders.OrderId.FULL)
    Mono<String> getOrderById(
            @PathVariable long orderId,
            @RequestParam(required = false, defaultValue = "false") boolean isNew,
            Model model) {

        return cartService.getOrderCart(orderId)
                .doOnNext(cartDto -> {
                    model.addAttribute("cart", cartDto);
                    model.addAttribute("isneworder", isNew);
                })
                .map(cartDto -> "order");
    }

    @GetMapping(StoreUrls.Orders.FULL)
    Mono<String> getOrders(Model model) {
        return orderService.getAllOrders()
                .collectList()
                .doOnNext(orders -> model.addAttribute("orders", orders))
                .doOnNext(orderDtos -> System.out.println(orderDtos))
                .map(orders -> "orders");
    }

}
