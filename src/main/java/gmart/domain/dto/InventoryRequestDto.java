package gmart.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryRequestDto(

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Category is required")
    String category,

    @NotNull(message = "Quantity is required")
    Integer quantity,

    @NotNull(message = "Price is required")
    Double price
) {}