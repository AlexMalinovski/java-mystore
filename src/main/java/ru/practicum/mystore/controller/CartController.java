package ru.practicum.mystore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.CartDto;
import ru.practicum.mystore.service.CartService;

import java.util.Optional;

@Controller
@SessionAttributes({"orderId"})
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping(StoreUrls.Cart.ItemId.FULL)
    String cartAction(@ModelAttribute CartAction cartAction, @PathVariable long itemId, Model model) {
        final Long orderId = (Long) model.getAttribute("orderId");
        model.addAttribute("orderId", cartService.handleCartAction(cartAction, itemId, orderId));

        return "redirect:" + Optional.ofNullable(cartAction.getSource()).orElse(StoreUrls.Main.FULL);
    }

    @GetMapping(StoreUrls.Cart.FULL)
    public String getCart(Model model) {
        final Long orderId = (Long) model.asMap().get("orderId");

        CartDto cart = cartService.getOrderCart(orderId);
        model.addAttribute("cart", cart);

        return "cart";
    }
}
