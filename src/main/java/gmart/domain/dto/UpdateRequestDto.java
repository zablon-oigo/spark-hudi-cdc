package gmart.domain.dto;

import gmart.domain.entity.Status;

public record UpdateRequestDto(
    String name,
    String category,
    Integer quantity,
    Double price,
    Status status
) {}