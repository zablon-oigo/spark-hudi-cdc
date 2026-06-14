package gmart.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import gmart.domain.dto.InventoryDto;
import gmart.domain.dto.InventoryRequestDto;
import gmart.domain.dto.QuantityRequestDto;
import gmart.domain.dto.UpdateRequestDto;
import gmart.service.InventoryService;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // CREATE 
    @PostMapping
    public ResponseEntity<InventoryDto> createInventory(
            @Valid @RequestBody InventoryRequestDto request
    ) {
        return new ResponseEntity<>(
                inventoryService.createInventory(request),
                HttpStatus.CREATED
        );
    }


    @PostMapping("/{inventoryId}/sell")
    public ResponseEntity<InventoryDto> sell(
            @PathVariable UUID inventoryId,
            @RequestBody QuantityRequestDto request
    ) {
        return ResponseEntity.ok(
                inventoryService.sellProduct(inventoryId, request.quantity())
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<InventoryDto>> listInventories() {
        return ResponseEntity.ok(inventoryService.listInventories());
    }

    // UPDATE 
    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryDto> updateInventory(
            @PathVariable UUID inventoryId,
            @Valid @RequestBody UpdateRequestDto request
    ) {
        return ResponseEntity.ok(
                inventoryService.updateInventory(inventoryId, request)
        );
    }


    @PostMapping("/{inventoryId}/restock")
    public ResponseEntity<InventoryDto> restock(
            @PathVariable UUID inventoryId,
            @RequestBody QuantityRequestDto request
    ) {
        return ResponseEntity.ok(inventoryService.restockProduct(inventoryId, request.quantity()));
    }

    // DELETE 
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable UUID inventoryId
    ) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }
}