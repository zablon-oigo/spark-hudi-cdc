package gmart.domain.dto;

import java.util.UUID;
import  gmart.domain.entity.Status;

public record InventoryDto(
    UUID id,
    String name,
    String category,
    int quantity,
    double price,
    Status status
) {}