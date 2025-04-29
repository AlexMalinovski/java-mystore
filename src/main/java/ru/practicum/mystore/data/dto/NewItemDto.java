package ru.practicum.mystore.data.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Getter
public class NewItemDto {
    @NotBlank
    @Size(min = 1, max = 200)
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin("1.0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    private final MultipartFile img;
}
