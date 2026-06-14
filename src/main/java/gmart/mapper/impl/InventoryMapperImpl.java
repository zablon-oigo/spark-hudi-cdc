package gmart.mapper.impl;

import org.springframework.stereotype.Component;

import gmart.domain.dto.InventoryDto;
import gmart.domain.dto.InventoryRequestDto;
import gmart.domain.dto.UpdateRequestDto;
import gmart.domain.entity.Inventory;
import gmart.mapper.InventoryMapper;

@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public Inventory toEntity(InventoryRequestDto dto){
        Inventory inv = new Inventory();
        inv.setName(dto.name());
        inv.setCategory(dto.category());
        inv.setPrice(dto.price());
        inv.setQuantity(dto.quantity());
        return inv;
    }

    @Override
    public Inventory toEntity(UpdateRequestDto dto){
        Inventory inv = new Inventory();
        inv.setName(dto.name());
        inv.setCategory(dto.category());
        inv.setPrice(dto.price());
        inv.setQuantity(dto.quantity());
        return inv;
    }

    @Override
    public InventoryDto toDto(Inventory inventory){
        return new InventoryDto(
        inventory.getId(),
        inventory.getName(),
        inventory.getCategory(),
        inventory.getQuantity(),
        inventory.getPrice(),
        inventory.getStatus()
);
    }
}