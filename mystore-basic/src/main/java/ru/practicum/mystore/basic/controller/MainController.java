package ru.practicum.mystore.basic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.constant.SortType;
import ru.practicum.mystore.basic.data.dto.MainDto;
import ru.practicum.mystore.basic.service.CartService;
import ru.practicum.mystore.basic.service.ItemService;

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

    @ModelAttribute("userName")
    public Mono<String> getUserName() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getName())
                .switchIfEmpty(Mono.just("Guest"));
    }

    @ModelAttribute("isAuthUser")
    public Mono<Boolean> getIsAuthUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().isAuthenticated())
                .switchIfEmpty(Mono.just(Boolean.FALSE));
    }

    @GetMapping(StoreUrls.FULL)
    public Mono<String> defaultRedirect() {
        return Mono.just("redirect:" + StoreUrls.Main.FULL);
    }

    @PostMapping(path = StoreUrls.Main.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<String> setItemFilter(@ModelAttribute MainDto mainDto) {
        return Mono.just("redirect:" + StoreUrls.Main.FULL);
    }

    @GetMapping(StoreUrls.Main.FULL)
    public Mono<String> getItems(Model model) {
        final var mainDto = (MainDto) model.asMap().getOrDefault("mainDto", getDefaultMainDto());
        final int pageSize = mainDto.getPageSize();
        final int pageNumber = mainDto.getPageNumber();
        final String nameFilter = Optional.ofNullable(mainDto.getNameSubstring()).orElse("");
        final SortType sortType = Optional.ofNullable(mainDto.getSort()).orElse(SortType.NO);


        return itemService.findPaginated(PageRequest.of(pageNumber - 1, pageSize), nameFilter, sortType)
                .flatMap(page -> {
                    final Long orderId = (Long) model.asMap().get("orderId");
                    if (orderId != null) {
                        return cartService.updatePageDataByCart(page, orderId);
                    }
                    return Mono.just(page);
                })
                .map(mainItems -> {
                    if (mainItems.getTotalPages() > mainItems.getNumber() || mainItems.getTotalPages() == 0) {
                        model.addAttribute("itemsPage", mainItems);
                        return "main";
                    } else {
                        mainDto.setPageNumber(1);
                        model.addAttribute("mainDto", mainDto);
                        return "redirect:" + StoreUrls.Main.FULL;
                    }
                });
    }
}
