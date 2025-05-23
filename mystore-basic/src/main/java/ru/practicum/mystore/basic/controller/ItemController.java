package ru.practicum.mystore.basic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.dto.NewItemDto;
import ru.practicum.mystore.basic.service.CartService;
import ru.practicum.mystore.basic.service.ItemService;

@Controller
@SessionAttributes({"orderId"})
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping(StoreUrls.Items.ItemId.FULL)
    public Mono<String> getItem(@PathVariable long itemId, Model model) {

        return itemService.findOnly(itemId)
                .flatMap(mainItemDto -> {
                    final Long orderId = (Long) model.asMap().get("orderId");
                    if (orderId != null) {
                        return cartService.updateMainItemDtoByCart(mainItemDto, orderId);
                    }
                    return Mono.just(mainItemDto);
                })
                .doOnNext(mainItemDto -> model.addAttribute("item", mainItemDto))
                .map(mainItemDto -> "item");
    }

    @GetMapping(StoreUrls.Items.Add.FULL)
    public Mono<String> getItemEditor(Model model) {
        model.addAttribute("newItemDto", NewItemDto.builder().build());
        return Mono.just("add-item");
    }

    @PostMapping(value = StoreUrls.Items.Add.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<String> addItem(@ModelAttribute("newItemDto") @Valid NewItemDto newItemDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("newItemDto", newItemDto);
            return Mono.just("add-item");
        }

        return itemService.addItem(newItemDto)
                .map(itemId -> "redirect:" +
                        StoreUrls.Items.ItemId.FULL.replaceAll("\\{itemId}", String.valueOf(itemId)));
    }
}
