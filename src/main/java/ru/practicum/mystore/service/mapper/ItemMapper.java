package ru.practicum.mystore.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.mystore.data.constant.AppConst;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.dto.NewItemDto;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.exception.BadRequestException;
import ru.practicum.mystore.tools.Util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(config = DefaultMapperConfig.class)
public interface ItemMapper {
    @Named("mapImageBytesToBase64")
    default String mapImageBytesToBase64(byte[] image) {
        if (image == null || image.length == 0) {
            return AppConst.NO_IMAGE_PLACEHOLDER;
        }
        return Base64.getEncoder().encodeToString(image);
    }

    @Named("mapMultipartToBytes")
    default byte[] mapMultipartToBytes(MultipartFile img) {
        if (img == null) {
            return null;
        }
        try {
            return img.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Изображение повреждено или отсутствует");
        }
    }

    @Named("toItemPrice")
    default String toItemPrice(Long price) {
        if (price == null) {
            return null;
        }
        return Util.toPrice(price);
    }

    @Named("mapPriceInternal")
    default Long mapPriceInternal(BigDecimal price) {
        if (price == null) {
            return null;
        }
        return Util.toPriceInternal(price);
    }

    @Mapping(target = "img", source = "img", qualifiedByName = "mapImageBytesToBase64")
    @Mapping(target = "priceView", source = "price", qualifiedByName = "toItemPrice")
    MainItemDto toMainItemDto(Item item);

    List<MainItemDto> toMainItemDto(List<Item> item);

    @Mapping(target = ".", source = "item")
    @Mapping(target = "cartCnt", source = "itemQty")
    @Mapping(target = "img", source = "item.img", qualifiedByName = "mapImageBytesToBase64")
    @Mapping(target = "priceView", source = "itemPrice", qualifiedByName = "toItemPrice")
    MainItemDto toMainItemDto(OrderItem src);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "cartCnt", source = "itemQty")
    @Mapping(target = "id", ignore = true)
    MainItemDto updateMainItemDto(@MappingTarget MainItemDto.MainItemDtoBuilder builder, OrderItem updates);

    @Mapping(target = "img", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPriceInternal")
    Item toItem(NewItemDto src);
}
