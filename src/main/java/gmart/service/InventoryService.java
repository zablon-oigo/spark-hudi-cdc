package gmart.service;

import java.util.List;
import java.util.UUID;

import gmart.domain.dto.InventoryDto;
import gmart.domain.dto.InventoryRequestDto;
import gmart.domain.dto.UpdateRequestDto;

public interface InventoryService {

    InventoryDto createInventory(InventoryRequestDto request);

    List<InventoryDto> listInventories();

    InventoryDto updateInventory(UUID inventoryId, UpdateRequestDto request);

    InventoryDto sellProduct(UUID inventoryId, int quantity);

    InventoryDto restockProduct(UUID inventoryId, int quantity);

    void deleteInventory(UUID inventoryId);
}