package gmart.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gmart.domain.dto.InventoryDto;
import gmart.domain.dto.InventoryRequestDto;
import gmart.domain.dto.UpdateRequestDto;
import gmart.domain.entity.Inventory;
import gmart.domain.entity.Status;
import gmart.exception.InventoryNotFoundException;
import gmart.mapper.InventoryMapper;
import gmart.repository.InventoryRepository;
import gmart.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            InventoryMapper inventoryMapper
    ) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    // CREATE
    @Override
    public InventoryDto createInventory(InventoryRequestDto request) {

        Inventory inventory = inventoryMapper.toEntity(request);

        inventory.setStatus(Status.IN_STOCK);
        inventory.setCreated(Instant.now());
        inventory.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inventory);

        InventoryDto dto = inventoryMapper.toDto(saved);

        return dto;
    }

    // LIST 
    @Override
    public List<InventoryDto> listInventories() {

        return inventoryRepository.findAll(Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(inventoryMapper::toDto)
                .toList();
    }

    // UPDATE 
    @Override
    public InventoryDto updateInventory(UUID inventoryId, UpdateRequestDto request) {

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        if (request.name() != null) inventory.setName(request.name());
        if (request.category() != null) inventory.setCategory(request.category());
        if (request.quantity() != null) inventory.setQuantity(request.quantity());
        if (request.price() != null) inventory.setPrice(request.price());
        if (request.status() != null) inventory.setStatus(request.status());

        inventory.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inventory);

        InventoryDto dto = inventoryMapper.toDto(saved);

        return dto;
    }

    // SELL 
    @Override
    public InventoryDto sellProduct(UUID id, int quantity) {

        Inventory inv = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        if (inv.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        int newQty = inv.getQuantity() - quantity;
        inv.setQuantity(newQty);
        inv.setStatus(newQty == 0 ? Status.OUT_OF_STOCK : Status.IN_STOCK);
        inv.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inv);

        InventoryDto dto = inventoryMapper.toDto(saved);

        return dto;
    }

    // RESTOCK 
    @Override
    public InventoryDto restockProduct(UUID id, int quantity) {

        Inventory inv = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        inv.setQuantity(inv.getQuantity() + quantity);
        inv.setStatus(Status.IN_STOCK);
        inv.setUpdated(Instant.now());

        Inventory saved = inventoryRepository.save(inv);

        InventoryDto dto = inventoryMapper.toDto(saved);


        return dto;
    }

    // DELETE 
    @Override
    public void deleteInventory(UUID inventoryId) {

        Inventory inv = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        inventoryRepository.deleteById(inventoryId);

    }
}