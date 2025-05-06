package ru.practicum.mystore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.data.constant.SortType;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.dto.NewItemDto;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.exception.NotFoundException;
import ru.practicum.mystore.repositories.ItemRepository;
import ru.practicum.mystore.service.ItemService;
import ru.practicum.mystore.service.mapper.ItemMapper;

import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    private BiFunction<Pageable, String, Mono<Page<Item>>> getFindMethodWithSort(SortType sortType) {
        return switch (sortType) {
            case ALPHA -> this::findAllByNameContainingOrderByNameAsc;
            case PRICE -> this::findAllByNameContainingOrderByPriceAsc;
            default -> this::findAllByNameContaining;
        };
    }

    private Mono<Page<Item>> findAllByNameContainingOrderByNameAsc(Pageable pageable, String nameFilter) {
        return itemRepository.findAllByNameContainingOrderByNameAsc(pageable, nameFilter)
                .collectList()
                .zipWith(itemRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    private Mono<Page<Item>> findAllByNameContainingOrderByPriceAsc(Pageable pageable, String nameFilter) {
        return itemRepository.findAllByNameContainingOrderByPriceAsc(pageable, nameFilter)
                .collectList()
                .zipWith(itemRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    private Mono<Page<Item>> findAllByNameContaining(Pageable pageable, String nameFilter) {
        return itemRepository.findAllByNameContaining(pageable, nameFilter)
                .collectList()
                .zipWith(itemRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    private Mono<byte[]> mapFilePartToBytes(FilePart img) {
        if (img == null) {
            return null;
        }

        return DataBufferUtils.join(img.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                });
    }

    @Override
    public Mono<Page<MainItemDto>> findPaginated(Pageable pageable, String nameFilter, SortType sortType) {
        return getFindMethodWithSort(sortType)
                .apply(pageable, nameFilter)
                .map(page -> page.map(itemMapper::toMainItemDto));
    }

    @Override
    public Mono<Item> findById(long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public Mono<MainItemDto> findOnly(long itemId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::toMainItemDto)
                .switchIfEmpty(Mono.error(
                        new NotFoundException(String.format("Товар с артикулом '%s' не найден", itemId))));
    }

    @Override
    public Mono<Long> addItem(NewItemDto itemDto) {
        Mono<byte[]> image = mapFilePartToBytes(itemDto.getImg());
        if (image == null) {
            image = Mono.empty();
        }
        return Mono.just(itemMapper.toItem(itemDto))
                .zipWith(image)
                .map(t -> t.getT1().toBuilder().img(t.getT2()).build())
                .flatMap(itemRepository::save)
                .map(Item::getId);
    }
}
