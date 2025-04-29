package ru.practicum.mystore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.practicum.mystore.data.constant.SortType;
import ru.practicum.mystore.data.dto.MainDto;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.service.CartService;
import ru.practicum.mystore.service.ItemService;

import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes({"mainDto", "orderId"})
@RequiredArgsConstructor
public class MainController {
    private static final List<Integer> DEFAULT_PAGE_SIZES = List.of(1, 2, 10, 20, 50, 100);
    private static final List<SortType> DEFAULT_SORTS = List.of(SortType.NO, SortType.ALPHA, SortType.PRICE);
    private final ItemService itemService;
    private final CartService cartService;


    @ModelAttribute("availableSizes")
    public List<Integer> getAvailablePageSizes() {
        return DEFAULT_PAGE_SIZES;
    }

    @ModelAttribute("availableSorts")
    public List<SortType> getAvailableSorts() {
        return DEFAULT_SORTS;
    }

    @ModelAttribute("mainDto")
    public MainDto getDefaultMainDto() {
        var dto = new MainDto();
        dto.setSort(SortType.NO);
        dto.setNameSubstring("");
        dto.setPageSize(DEFAULT_PAGE_SIZES.getFirst());
        dto.setPageNumber(1);


        return dto;
    }

    @PostMapping(path = StoreUrls.Main.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String setItemFilter(@ModelAttribute MainDto mainDto) {
        return "redirect:" + StoreUrls.Main.FULL;
    }

    @GetMapping(StoreUrls.Main.FULL)
    public String getItems(Model model) {
        final var mainDto = (MainDto) model.asMap().getOrDefault("mainDto", getDefaultMainDto());
        final int pageSize = mainDto.getPageSize();
        final int pageNumber = mainDto.getPageNumber();
        final String nameFilter = Optional.ofNullable(mainDto.getNameSubstring()).orElse("");
        final SortType sortType = Optional.ofNullable(mainDto.getSort()).orElse(SortType.NO);

        Page<MainItemDto> mainItems = itemService.findPaginated(
                PageRequest.of(pageNumber - 1, pageSize), nameFilter, sortType);
        final Long orderId = (Long) model.asMap().get("orderId");
        if (orderId != null) {
            mainItems = cartService.updatePageDataByCart(mainItems, orderId);
        }

        if (mainItems.getTotalPages() > mainItems.getNumber() || mainItems.getTotalPages() == 0) {
            model.addAttribute("itemsPage", mainItems);
        } else {
            mainDto.setPageNumber(1);
            model.addAttribute("mainDto", mainDto);
            return "redirect:" + StoreUrls.Main.FULL;
        }
        return "main";
    }
}
