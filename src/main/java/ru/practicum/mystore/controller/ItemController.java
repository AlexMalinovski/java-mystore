package ru.practicum.mystore.controller;

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
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.dto.NewItemDto;
import ru.practicum.mystore.service.CartService;
import ru.practicum.mystore.service.ItemService;

@Controller
@SessionAttributes({"orderId"})
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping(StoreUrls.Items.ItemId.FULL)
    public String getItem(@PathVariable long itemId, Model model) {
        MainItemDto mainItemDto = itemService.findOnly(itemId);
        final Long orderId = (Long) model.asMap().get("orderId");
        if (orderId != null) {
            mainItemDto = cartService.updateMainItemDtoByCart(mainItemDto, orderId);
        }
        model.addAttribute("item", mainItemDto);
        return "item";
    }

    @GetMapping(StoreUrls.Items.Add.FULL)
    public String getItemEditor(Model model) {
        model.addAttribute("newItemDto", NewItemDto.builder().build());
        return "add-item";
    }

    @PostMapping(value = StoreUrls.Items.Add.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addItem(@ModelAttribute("newItemDto") @Valid NewItemDto newItemDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("newItemDto", newItemDto);
            return "add-item";
        }
        Long itemId = itemService.addItem(newItemDto);
        return "redirect:" + StoreUrls.Items.ItemId.FULL.replaceAll("\\{itemId}", String.valueOf(itemId));
    }
}
