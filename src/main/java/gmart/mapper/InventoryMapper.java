package gmart.mapper;

import gmart.domain.dto.InventoryDto;
import gmart.domain.dto.InventoryRequestDto;
import gmart.domain.dto.UpdateRequestDto;
import gmart.domain.entity.Inventory;

public interface InventoryMapper {

    Inventory toEntity(InventoryRequestDto dto);

    Inventory toEntity(UpdateRequestDto dto);

    InventoryDto toDto(Inventory inventory);
}